package project.assay.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.DAYS;

@Component
public class CookieUtil {

    @Value("${app.cookie.secure}")
    private boolean secure;

    @Value("${app.cookie.ttl-days}")
    private long cookieTtlDays;

    public Cookie createRefreshTokenCookie(String refreshtoken) {
        Cookie cookie = new Cookie("refreshtoken", refreshtoken);
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge((int) DAYS.toSeconds(cookieTtlDays));

        cookie.setAttribute("SameSite", "None");

        return cookie;
    }

    public Cookie deleteRedreshTokenCookie() {
        Cookie cookie = new Cookie("refreshtoken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
