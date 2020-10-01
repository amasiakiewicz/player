package com.casinoroyale.player;

import java.time.Clock;
import java.time.ZoneOffset;

import javax.validation.ClockProvider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlayerApplication {

    public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.UTC;

    public static void main(String[] args) {
        SpringApplication.run(PlayerApplication.class, args);
    }

    @Bean
    ClockProvider clockProvider() {
        return () -> Clock.system(DEFAULT_ZONE_OFFSET);
    }

}
