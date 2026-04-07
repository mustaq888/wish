package com.wishlist.ai.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_MINUTE = 120;
    private final Map<String, ClientWindow> clients = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var key = request.getRemoteAddr();
        var currentMinute = Instant.now().getEpochSecond() / 60;
        var window = clients.computeIfAbsent(key, ignored -> new ClientWindow(currentMinute, new AtomicInteger(0)));

        synchronized (window) {
            if (window.minute() != currentMinute) {
                window.reset(currentMinute);
            }
            if (window.counter().incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Rate limit exceeded");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private static final class ClientWindow {
        private long minute;
        private final AtomicInteger counter;

        private ClientWindow(long minute, AtomicInteger counter) {
            this.minute = minute;
            this.counter = counter;
        }

        long minute() {
            return minute;
        }

        AtomicInteger counter() {
            return counter;
        }

        void reset(long newMinute) {
            this.minute = newMinute;
            this.counter.set(0);
        }
    }
}
