package project.assay.services;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.LoginRequestDTO;
import project.assay.dto.requests.RegisterRequestDTO;
import project.assay.dto.responses.AccessTokenDTO;
import project.assay.dto.responses.MessageDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.Customer;
import project.assay.repositories.CustomerRepository;
import project.assay.utils.CookieUtil;

import static java.util.Objects.nonNull;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final VerificationService verificationService;
    private final CookieUtil cookieUtil;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService, RefreshTokenService refreshTokenService,
                       VerificationService verificationService,
                       CookieUtil cookieUtil,
                       CustomerRepository customerRepository, CustomerService customerService) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.verificationService = verificationService;

        this.cookieUtil = cookieUtil;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    @Transactional
    public MessageDTO register(RegisterRequestDTO dto) {

        if (customerRepository.existsActiveByEmail(dto.getEmail())) {
            throw new EntityNotFoundException(
                    "Пользователь с email: '%s' уже существует!"
                            .formatted(dto.getEmail()));
        }

        Customer customer = new Customer();
        customer.setEmail(dto.getEmail());
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));

        customerRepository.save(customer);

        verificationService.sendVerifTokenToCustomer(customer);

        return new MessageDTO(
                        "На вашу почту '%s' было отправлено письмо для подтверждения!"
                                .formatted(customer.getEmail()));
    }


    @Transactional
    public AccessTokenDTO login(LoginRequestDTO dto, HttpServletResponse response) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        Customer customer = customerService
                .findByEmail(dto.getEmail());

        if (!customer.isEnabled()) {
            throw new EntityNotFoundException(
                    "Аккаунт с email: '%s' не активирован!"
                            .formatted(dto.getEmail()));
        }

        String accessToken = jwtService.generateToken(customer);
        String refreshToken = refreshTokenService.generateRefreshToken(customer);

        response.addCookie(cookieUtil.createRefreshTokenCookie(refreshToken));

        return new AccessTokenDTO(accessToken);
    }

    public AccessTokenDTO refresh(String requestToken, HttpServletResponse response) {

        var refreshToken = refreshTokenService.findAndValidateRefreshToken(requestToken);
        Customer customer = refreshToken.getCustomer();
        String newAccessToken = jwtService.generateToken(customer);

        response.addCookie(cookieUtil.createRefreshTokenCookie(refreshToken.getToken()));

        return new AccessTokenDTO(newAccessToken);
    }


    @Transactional
    public MessageDTO logout(String refreshToken, HttpServletResponse response) {
        if (nonNull(refreshToken)) refreshTokenService.revoke(refreshToken);

        response.addCookie(cookieUtil.deleteRedreshTokenCookie());

        return new MessageDTO("Успешный выход из учетной записи!");
    }
}
