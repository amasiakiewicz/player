package com.casinoroyale.player.player;

import java.util.UUID;

import com.casinoroyale.player.player.domain.PlayerFacade;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class PlayerListener {

    private final PlayerFacade playerFacade;

    PlayerListener(final PlayerFacade playerFacade) {
        this.playerFacade = playerFacade;
    }

    @KafkaListener(topics = "PlayerTransferred")
    public void listenCreated(ConsumerRecord<UUID, UUID> kafkaMessage) {
        final UUID playerId = kafkaMessage.key();
        final UUID newTeamId = kafkaMessage.value();

        playerFacade.changePlayersTeam(playerId, newTeamId);
    }

    @KafkaListener(topics = "TeamDeleted")
    public void listenDeleted(ConsumerRecord<String, UUID> kafkaMessage) {
        final UUID teamId = kafkaMessage.value();
        playerFacade.deletePlayersAndTeam(teamId);
    }

}
