package project.assay.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;
import static java.util.regex.Pattern.compile;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

@OpenAPIDefinition(
        info = @Info(
                title = "TRANSAY API",
                version = "0.1",
                description = "API для системы расшифровки медицинский анализов",
                contact = @Contact(
                        name = "Поддержка",
                        email = "rejngleb@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8081", description = "Локальная разработка"),
                @Server(url = "https://api.example.com", description = "Production")
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@Configuration
public class ApiSpecification {
    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> {

            PreAuthorize preAuthorize =
                    findMergedAnnotation(
                            handlerMethod.getMethod(),
                            PreAuthorize.class
                    );

            if (preAuthorize == null) {
                preAuthorize =
                        findMergedAnnotation(
                                handlerMethod.getBeanType(),
                                PreAuthorize.class
                        );
            }

            if (preAuthorize != null) {
                List<String> baseRoles = extractRolesList(preAuthorize.value());

                List<String> expandedRoles = expandRoles(baseRoles);

                String roles = String.join(", ", expandedRoles);

                String description = operation.getDescription() == null
                        ? ""
                        : operation.getDescription() + "\n\n";

                operation.setDescription(
                        description + "🔐 Доступно для: " + roles
                );
            }

            return operation;
        };
    }

    private List<String> extractRolesList(String expression) {
        Pattern pattern = compile("'([^']*)'");
        Matcher matcher = pattern.matcher(expression);

        List<String> roles = new ArrayList<>();

        while (matcher.find()) {
            roles.add(matcher.group(1));
        }

        return roles;
    }

    private List<String> expandRoles(List<String> baseRoles) {
        List<String> allRoles = new ArrayList<>();

        for (String role : baseRoles)
            switch (role) {
                case "USER" -> {
                    allRoles.add("USER");
                    allRoles.add("EDITOR");
                    allRoles.add("ADMIN");
                }
                case "EDITOR" -> {
                    allRoles.add("EDITOR");
                    allRoles.add("ADMIN");
                }
                case "ADMIN" -> allRoles.add("ADMIN");
            }

        return allRoles.stream().distinct().toList();
    }
}
