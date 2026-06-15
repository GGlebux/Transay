package project.assay.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import project.assay.models.HttpLog;
import project.assay.repositories.HttpLogRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class HttpLoggingFilter extends OncePerRequestFilter {

    private static final int MAX_BODY = 20_000;

    private final HttpLogRepository repository;

    public HttpLoggingFilter(HttpLogRepository repository) {
        this.repository = repository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return !uri.startsWith("/api/") || uri.startsWith("/api/admin/http-logs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper resp = new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();
        Exception error = null;
        try {
            filterChain.doFilter(req, resp);
        } catch (Exception e) {
            error = e;
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - start;
            int status = resp.getStatus();
            if (error != null && status < 400) {
                status = 500;
            }
            try {
                save(req, resp, status, duration);
            } catch (Exception ignored) {
            }
            resp.copyBodyToResponse();
        }
    }

    private void save(ContentCachingRequestWrapper req,
                      ContentCachingResponseWrapper resp,
                      int status,
                      long duration) {

        HttpLog log = new HttpLog();
        log.setCreatedAt(LocalDateTime.now());
        log.setMethod(req.getMethod());
        log.setUri(req.getRequestURI());
        log.setQueryString(req.getQueryString());
        log.setStatus(status);
        log.setDurationMs(duration);
        log.setClientIp(clientIp(req));
        log.setPrincipal(principal());
        log.setError(status >= 500);

        if (!req.getRequestURI().startsWith("/api/auth/")) {
            log.setRequestBody(asText(req.getContentAsByteArray(), req.getCharacterEncoding()));
            log.setResponseBody(asText(resp.getContentAsByteArray(), resp.getCharacterEncoding()));
        }

        repository.save(log);
    }

    private String clientIp(HttpServletRequest req) {
        String forwarded = req.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return req.getRemoteAddr();
    }

    private String principal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return auth.getName();
    }

    private String asText(byte[] body, String encoding) {
        if (body == null || body.length == 0) {
            return null;
        }
        Charset charset = encoding != null ? Charset.forName(encoding) : StandardCharsets.UTF_8;
        String text = new String(body, charset);
        if (text.length() > MAX_BODY) {
            return text.substring(0, MAX_BODY) + "...[truncated]";
        }
        return text;
    }
}
