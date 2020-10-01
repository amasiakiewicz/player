package com.casinoroyale.player.team;

import com.casinoroyale.player.team.domain.TeamFacade;
import com.casinoroyale.team.team.dto.CreateTeamNoticeDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class TeamListener {

    private final TeamFacade teamFacade;

    TeamListener(final TeamFacade teamFacade) {
        this.teamFacade = teamFacade;
    }

    @KafkaListener(topics = "TeamCreated")
    public void listenCreated(ConsumerRecord<String, CreateTeamNoticeDto> kafkaMessage) {
        final CreateTeamNoticeDto createTeamNoticeDto = kafkaMessage.value();
        teamFacade.createTeam(createTeamNoticeDto);
    }

}
