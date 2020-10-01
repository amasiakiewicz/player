package com.casinoroyale.player.team.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TeamConfiguration {

    @Bean
    TeamFacade teamFacade(final TeamRepository teamRepository) {
        return new TeamFacade(teamRepository);
    }

}
