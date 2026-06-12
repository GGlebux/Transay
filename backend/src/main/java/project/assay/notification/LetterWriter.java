package project.assay.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LetterWriter {
    private static final String VERIFICATION_HTML;
    private static final String PASS_RESET_HTML;
    private final EmailSender emailSender;

    @Value("${app.front-url}")
    private String frontUrl;

    static {
        VERIFICATION_HTML = """
                <html>
                <body style="font-family: Arial, sans-serif;">
                    <h2>Система расшифровки анализов. Подтверждение аккаунта</h2>
                    <p>Ссылка действительна 5 минут.</p>
                    <p>Нажмите кнопку ниже, чтобы подтвердить email:</p>
                    <a href="%s"
                       style="
                        display:inline-block;
                        padding:12px 20px;
                        background-color:#4CAF50;
                        color:white;
                        text-decoration:none;
                        border-radius:5px;">
                        Подтвердить email
                    </a>
                    <p>Если вы не регистрировались — проигнорируйте это письмо.</p>
                </body>
                </html>
                """;
        PASS_RESET_HTML = """
                <html>
                <body style="font-family: Arial, sans-serif;">
                    <h2>Система расшифровки анализов. Сброс пароля</h2>
                    <p>Ссылка действительна 5 минут.</p>
                    <p>Нажмите кнопку ниже, чтобы сбросить пароль:</p>
                    <a href="%s"
                       style="
                        display:inline-block;
                        padding:12px 20px;
                        background-color:#4CAF50;
                        color:white;
                        text-decoration:none;
                        border-radius:5px;">
                        Сбросить пароль
                    </a>
                    <p>Если вы не этого не делали — проигнорируйте это письмо.</p>
                </body>
                </html>
                """;
    }

    @Autowired
    public LetterWriter(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendVerificationEmail(String recipient, String token) {
        String link = frontUrl + "/verify?token=" + token;
        emailSender.sendHtml(recipient, "Подтверждение регистрации", VERIFICATION_HTML.formatted(link));
    }

    public void sendPassResetEmail(String recipient, String token) {
        String link = frontUrl + "/forgot-password?token=" + token;
        emailSender.sendHtml(recipient, "Подтверждение регистрации", PASS_RESET_HTML.formatted(link));
    }
}
