package ru.yandex.yandexlavka.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.yandex.yandexlavka.utils.interceptors.RateLimitInterceptor;

import java.time.Duration;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Refill refill = Refill.greedy(10, Duration.ofSeconds(1));
        Bandwidth limit = Bandwidth.classic(10, refill).withInitialTokens(1);
        Bucket bucket = Bucket.builder().addLimit(limit).build();
        registry.addInterceptor(new RateLimitInterceptor(bucket, 1))
                .addPathPatterns("/couriers");

        refill = Refill.greedy(10, Duration.ofSeconds(1));
        limit = Bandwidth.classic(10, refill).withInitialTokens(1);
        bucket = Bucket.builder().addLimit(limit).build();
        registry.addInterceptor(new RateLimitInterceptor(bucket, 1))
                .addPathPatterns("/couriers/{courier_id}");

        refill = Refill.greedy(10, Duration.ofSeconds(1));
        limit = Bandwidth.classic(10, refill).withInitialTokens(1);
        bucket = Bucket.builder().addLimit(limit).build();
        registry.addInterceptor(new RateLimitInterceptor(bucket, 1))
                .addPathPatterns("/couriers/assignments");

        refill = Refill.greedy(10, Duration.ofSeconds(1));
        limit = Bandwidth.classic(10, refill).withInitialTokens(1);
        bucket = Bucket.builder().addLimit(limit).build();
        registry.addInterceptor(new RateLimitInterceptor(bucket, 1))
                .addPathPatterns("/couriers/meta-info/{courier_id}");

        refill = Refill.greedy(10, Duration.ofSeconds(1));
        limit = Bandwidth.classic(10, refill).withInitialTokens(1);
        bucket = Bucket.builder().addLimit(limit).build();
        registry.addInterceptor(new RateLimitInterceptor(bucket, 1))
                .addPathPatterns("/orders");

        refill = Refill.greedy(10, Duration.ofSeconds(1));
        limit = Bandwidth.classic(10, refill).withInitialTokens(1);
        bucket = Bucket.builder().addLimit(limit).build();
        registry.addInterceptor(new RateLimitInterceptor(bucket, 1))
                .addPathPatterns("/orders/{order_id}");

        refill = Refill.greedy(10, Duration.ofSeconds(1));
        limit = Bandwidth.classic(10, refill).withInitialTokens(1);
        bucket = Bucket.builder().addLimit(limit).build();
        registry.addInterceptor(new RateLimitInterceptor(bucket, 1))
                .addPathPatterns("/orders/complete");

        refill = Refill.greedy(10, Duration.ofSeconds(1));
        limit = Bandwidth.classic(10, refill).withInitialTokens(1);
        bucket = Bucket.builder().addLimit(limit).build();
        registry.addInterceptor(new RateLimitInterceptor(bucket, 1))
                .addPathPatterns("/orders/assign");
    }
}
