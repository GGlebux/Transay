package project.assay.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.*;
import project.assay.dto.responses.AccessTokenDTO;
import project.assay.dto.responses.MessageDTO;
import project.assay.services.AuthService;
import project.assay.services.VerificationService;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация", description = "Всё доступно без авторизации и ролей")
@SecurityRequirements()
public class AuthController {
    private final AuthService authService;
    private final VerificationService verificationService;

    @Autowired
    public AuthController(AuthService authService, VerificationService verificationService) {
        this.authService = authService;
        this.verificationService = verificationService;
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя",
            description = "Автоматически отправляет письмо на почту для подтверждения"
    )
    public ResponseEntity<MessageDTO> register(@RequestBody @Valid RegisterRequestDTO dto) {
        return ok(authService.register(dto));
    }

    @PostMapping("/verification")
    @Operation(summary = "Подтвердить почту",
            description = "Необходимо отловить токен на фронте и закинуть сюда.\nСсылка в формате ${front_url}/verify?token=........"
    )
    public ResponseEntity<MessageDTO> verification(@RequestBody @Valid ConfirmTokenDTO dto) {
        return ok(verificationService.confirm(dto));
    }

    @PostMapping("/verification/send")
    @Operation(summary = "Отправить повторно письмо для верификации")
    public ResponseEntity<MessageDTO> sendVerification(@RequestBody @Valid EmailDTO dto) {
        return ok(verificationService.sendVerifTokenToEmail(dto));
    }

    @PostMapping("/login")
    @Operation(summary = "Залогиниться",
            description = "Отправляет два токена: access - в теле ответа и refresh в httpOnly cookies"
    )
    public ResponseEntity<AccessTokenDTO> login(@RequestBody @Valid LoginRequestDTO dto,
                                                 HttpServletResponse response) {
        return ok(authService.login(dto, response));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Обновить токены доступа",
            description = "Доступен только если refresh токен жив")
    public ResponseEntity<AccessTokenDTO> refresh(@CookieValue(name = "refreshtoken") String refreshToken,
                                                  HttpServletResponse response) {
        return ok(authService.refresh(refreshToken, response));
    }

    @PostMapping("/reset/start")
    @Operation(summary = "Отправить письмо для сброса пароля",
            description = "В письме указана ссылка в формате ${front_url}/forgot-password?token=........")
    public ResponseEntity<MessageDTO> passwordResetStart(@RequestBody @Valid EmailDTO dto) {
        return ok(verificationService.sendResetTokenToEmail(dto));
    }

    @PostMapping("/reset/end")
    @Operation(summary = "Ввести новый пароль и код из письма")
    public ResponseEntity<MessageDTO> passwordResetEnd(@RequestBody @Valid NewPassDTO dto) {
        return ok(verificationService.changePassword(dto));
    }

    @PostMapping("/logout")
    @Operation(summary = "Разлогиниться",
        description = "Удаляет refresh токен, access токен удаляешь на своей стороне!")
    public ResponseEntity<MessageDTO> logout(@CookieValue(name = "refreshtoken", required = false) String refreshToken,
                                             HttpServletResponse response) {
        return ok(authService.logout(refreshToken, response));
    }
}
