package project.assay.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import project.assay.dto.responses.ProblemDetailResponse;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.repositories.CustomerRepository;
import project.assay.services.JwtService;

import java.io.IOException;
import java.util.Collection;

import static java.util.List.of;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl.fromHierarchy;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.oauth2.jwt.NimbusJwtDecoder.withSecretKey;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final AppConfig appConfig;
    private final ObjectMapper objectMapper;

    @Autowired
    public SecurityConfig(JwtService jwtService,
                          CustomerRepository customerRepository,
                          AppConfig appConfig, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
        this.appConfig = appConfig;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        return security
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(this.authenticationEntryPoint())
                        .accessDeniedHandler(this.accessDeniedHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        // Публичные
                        .requestMatchers(OPTIONS, "/**").permitAll()
                        .requestMatchers("/actuator/health",
                                "/actuator/health/**",
                                "/api/auth/**",
                                "/api/verification/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml").permitAll()
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(STATELESS)
                )
                .build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        String hierarchy = """
            ROLE_ADMIN > ROLE_EDITOR
            ROLE_EDITOR > ROLE_USER
        """;
        return fromHierarchy(hierarchy);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return withSecretKey(jwtService.getSigningKey()).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        converter.setPrincipalClaimName("sub");
        return converter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> customerRepository
                .findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Пользователь не найден: '%s'"
                                .formatted(username)));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return (request, response, authException) -> {
            HttpStatus status = UNAUTHORIZED;

            ProblemDetailResponse problemDetail = new ProblemDetailResponse(
                    status.getReasonPhrase(),
                    status.value(),
                    authException.getMessage() != null
                            ? authException.getMessage()
                            : "Для доступа к этому ресурсу требуется полная аутентификация!",
                    request.getRequestURI()
            );

            createResponse(response, status, problemDetail);
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return (request, response, accessDeniedException) -> {
            HttpStatus status = FORBIDDEN;

            ProblemDetailResponse problemDetail = new ProblemDetailResponse(
                    status.getReasonPhrase(),
                    status.value(),
                    accessDeniedException.getMessage() != null ?
                            accessDeniedException.getMessage() :
                            "Доступ запрещен!",
                    request.getRequestURI()
            );

            createResponse(response, status, problemDetail);
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(of(appConfig.getFrontUrl(), "https://web.postman.co"));
        config.setAllowedMethods(of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(of("Set-Cookie"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private void createResponse(HttpServletResponse response, HttpStatus status, ProblemDetailResponse problemDetail) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/problem+json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
        response.getWriter().flush();
    }

    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("role");
        converter.setAuthorityPrefix("ROLE_");
        return converter;
    }
}
