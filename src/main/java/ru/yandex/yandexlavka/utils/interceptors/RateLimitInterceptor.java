package ru.yandex.yandexlavka.utils.interceptors;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class RateLimitInterceptor implements HandlerInterceptor {
    private final Bucket bucket;

    private final int numTokens;

    public RateLimitInterceptor(Bucket bucket, int numTokens) {
        this.bucket = bucket;
        this.numTokens = numTokens;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ConsumptionProbe probe = this.bucket.tryConsumeAndReturnRemaining(this.numTokens);
        if (probe.isConsumed()) {
            return true;
        }
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        return false;
    }
}
