package com.piotrekcieslak.gusintegrationspring.config;

import com.piotrekcieslak.gusintegrationspring.exception.GusException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class GusRateLimiter {

    private final Bucket bucket;

    public GusRateLimiter() {
        // Limit 1: Max 3 zapytania na sekundę
        Bandwidth limitPerSecond = Bandwidth.builder()
                .capacity(3)
                .refillIntervally(3, Duration.ofSeconds(1))
                .build();

        // Limit 2: Max 100 zapytań na minutę (bezpieczny bufor dla wymogu 120/min)
        Bandwidth limitPerMinute = Bandwidth.builder()
                .capacity(100)
                .refillIntervally(100, Duration.ofMinutes(1))
                .build();

        // Limit 3: Max 6000 zapytań na godzinę
        Bandwidth limitPerHour = Bandwidth.builder()
                .capacity(6000)
                .refillIntervally(6000, Duration.ofHours(1))
                .build();

        // Budujemy jeden koszyk, który egzekwuje wszystkie 3 limity JEDNOCZEŚNIE
        this.bucket = Bucket.builder()
                .addLimit(limitPerSecond)
                .addLimit(limitPerMinute)
                .addLimit(limitPerHour)
                .build();
    }

    /**
     * Zatrzymuje wirtualny wątek w kolejce, dopóki nie zwolni się miejsce w limitach GUS.
     */
    public void waitForToken() {
        try {
            // asBlocking() to magia - jeśli nie ma tokenów, wątek "zasypia" i czeka w kolejce.
            // Ustawiamy maksymalny czas oczekiwania na 15 sekund, żeby użytkownik nie wisiał w nieskończoność.
            boolean hasToken = bucket.asBlocking().tryConsume(1, Duration.ofSeconds(15));
            
            if (!hasToken) {
                log.warn("Kolejka do GUS jest zbyt długa. Odrzucono zapytanie po 15s oczekiwania.");
                throw new GusException("Serwer jest obecnie przeciążony ogromną liczbą zapytań. Spróbuj ponownie za chwilę.");
            }
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
            throw new GusException("Przerwano oczekiwanie w kolejce na połączenie z GUS.");
        }
    }
}