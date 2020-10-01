package com.casinoroyale.player.player.domain;

import com.casinoroyale.player.team.domain.TeamFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
class PlayerConfiguration {

    @Bean
    PlayerFacade playerFacade(
            final PlayerRepository playerRepository, final KafkaTemplate<Object, Object> kafkaTemplate, final TeamFacade teamFacade
    ) {
        return new PlayerFacade(playerRepository, kafkaTemplate, teamFacade);
    }

}
