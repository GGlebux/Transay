package project.assay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.ConfirmTokenDTO;
import project.assay.dto.requests.EmailDTO;
import project.assay.dto.requests.NewPassDTO;
import project.assay.dto.responses.MessageDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.exceptions.InvalidOperationException;
import project.assay.exceptions.InvalidVerificationTokenException;
import project.assay.models.Customer;
import project.assay.models.VerificationToken;
import project.assay.notification.LetterWriter;
import project.assay.repositories.VerificationTokenRepository;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;


@Service
@Transactional(readOnly = true)
public class VerificationService {
    private final VerificationTokenRepository repo;
    private final LetterWriter letterWriter;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public VerificationService(VerificationTokenRepository repo,
                               LetterWriter letterWriter,
                               CustomerService customerService,
                               PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.letterWriter = letterWriter;

        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public MessageDTO sendVerifTokenToEmail(EmailDTO dto) {
        var customer = customerService.findByEmail(dto.getEmail());

        if (customer.isEnabled()) {
            throw new InvalidOperationException(
                    "Учетная запись с почтой '%s' уже подтверждена"
                            .formatted(dto.getEmail()));
        }

        return this.sendVerifTokenToCustomer(customer);
    }

    @Transactional
    public MessageDTO sendResetTokenToEmail(EmailDTO dto) {
        var customer = customerService.findByEmail(dto.getEmail());

        var token = new VerificationToken(customer, randomUUID().toString());
        repo.save(token);

        letterWriter.sendPassResetEmail(customer.getEmail(), token.getToken());

        return new MessageDTO(
                        "На вашу почту '%s' было отправлено письмо для сброса пароля!"
                                .formatted(customer.getEmail()));
    }

    @Transactional
    public MessageDTO changePassword(NewPassDTO dto) {
        var token = repo
                .findByToken(dto.getToken())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Недействительная ссылка для сброса пароля!"));

        if (token.getExpiresAt().isBefore(now())) {
            repo.delete(token);
            throw new InvalidVerificationTokenException("Ссылка сброса пароля устарела!");
        }

        var customer = token.getCustomer();

        if (!customer.isEnabled()) {
            throw new InvalidOperationException("Чтобы сбросить пароль, подтвердите аккаунт!");
        }

        customer.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        customerService.save(customer);

        return new MessageDTO("Ваш пароль успешно изменен!");
    }


    @Transactional
    public MessageDTO sendVerifTokenToCustomer(Customer customer) {
        var token = new VerificationToken(customer, randomUUID().toString());
        repo.save(token);

        letterWriter.sendVerificationEmail(customer.getEmail(), token.getToken());

        return 
                new MessageDTO(
                        "На вашу почту '%s' было отправлено письмо для подтверждения!"
                                .formatted(customer.getEmail()));
    }

    @Transactional
    public MessageDTO confirm(ConfirmTokenDTO request) {
        var token = repo
                .findByToken(request.getToken())
                .orElseThrow(() -> new EntityNotFoundException("Недействительная ссылка подтверждения!"));

        if (token.getExpiresAt().isBefore(now())) {
            repo.delete(token);
            throw new InvalidVerificationTokenException("Ссылка подтверждения устарел!");
        }

        Customer customer = token.getCustomer();
        customer.enable();
        customerService.save(customer);

        return new MessageDTO("Ваш аккаунт успешно подтвержден!");
    }
}
