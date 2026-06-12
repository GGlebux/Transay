package project.assay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.exceptions.InvalidAccessTokenException;
import project.assay.models.Customer;
import project.assay.models.RefreshToken;
import project.assay.repositories.RefreshTokenRepository;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;


@Service
@Transactional(readOnly = true)
public class RefreshTokenService {
    private final RefreshTokenRepository repo;
    @Value("${jwt.refresh-ttl-days}")
    private long refreshTtlDays;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public String generateRefreshToken(Customer customer) {
        repo.deleteByCustomer(customer);

        RefreshToken token = new RefreshToken();
        token.setCustomer(customer);
        token.setToken(randomUUID().toString());
        token.setExpiresAt(now().plusDays(refreshTtlDays));

        repo.save(token);

        return token.getToken();
    }

    public RefreshToken findAndValidateRefreshToken(String token) {

        RefreshToken refreshToken = repo.findByToken(token)
                .orElseThrow(() -> new InvalidAccessTokenException(
                        "Некорректный refresh токен!"));

        if (refreshToken.getExpiresAt().isBefore(now())) {
            throw new InvalidAccessTokenException("Refresh токен устарел!");
        }

        return refreshToken;
    }

    @Transactional
    public void revoke(String token) {
        repo.findByToken(token);
    }
}
