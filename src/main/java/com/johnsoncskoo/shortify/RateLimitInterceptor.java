package com.johnsoncskoo.shortify;

import com.johnsoncskoo.shortify.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RateLimiterService rateLimiterService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var ipAddress = getClientIpAddress(request);
        var path = request.getRequestURI();
        var method = request.getMethod();
        // create redis key
        var key = "rate-limit:ip:" + ipAddress + ":method:" + method + ":path:" + path;

        // consume token
        if (!rateLimiterService.tryConsume(key, path, method)) {
            long remainingSeconds = 60 - (System.currentTimeMillis() / 1000) % 60;

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(remainingSeconds));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                    "Rate limit exceeded. Try again in " + remainingSeconds + " seconds.");
            return false;
        }

        long remainingTokens = rateLimiterService.getRemainingLimit(key, path, method);
        response.setHeader("X-Rate-Limit-Remaining", String.valueOf(remainingTokens));
        return true;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            return xForwardedForHeader.split(",")[0].trim();
        }
    }
}
