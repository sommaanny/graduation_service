package graduation_service.graduation.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class CsrfTokenDebugFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("===== CSRF Token Debug Filter Activated for URI: {} =====", request.getRequestURI());

        // 1. 요청 헤더에서 X-XSRF-TOKEN 값을 가져옵니다.
        String headerToken = request.getHeader("X-XSRF-TOKEN");
        log.info("Request Header [X-XSRF-TOKEN]: {}", headerToken);

        // 2. 요청의 쿠키에서 XSRF-TOKEN 값을 가져옵니다.
        String cookieToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            cookieToken = Arrays.stream(cookies)
                    .filter(cookie -> "XSRF-TOKEN".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        log.info("Request Cookie [XSRF-TOKEN]: {}", cookieToken);

        if (headerToken != null && headerToken.equals(cookieToken)) {
            log.info(">>> SUCCESS: Header token and Cookie token are MATCHING! <<<");
        } else {
            log.warn(">>> FAILED: Header token and Cookie token are MISMATCHING or one is NULL! <<<");
        }
        log.info("================================================================");

        filterChain.doFilter(request, response);
    }
}
