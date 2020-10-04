package com.casinoroyale.player.player;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.UUID;

import com.casinoroyale.player.player.domain.PlayerFacade;
import com.casinoroyale.transfer.team.dto.FeePlayerTransferredNoticeDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class PlayerListener {

    private final PlayerFacade playerFacade;

    PlayerListener(final PlayerFacade playerFacade) {
        this.playerFacade = playerFacade;
    }

    @KafkaListener(topics = "FeeAndPlayerTransferred")
    public void listenTransferred(ConsumerRecord<String, FeePlayerTransferredNoticeDto> kafkaMessage) {
        final FeePlayerTransferredNoticeDto feePlayerTransferredNoticeDto = kafkaMessage.value();
        checkArgument(feePlayerTransferredNoticeDto != null);

        final UUID playerId = feePlayerTransferredNoticeDto.getPlayerId();
        final UUID newTeamId = feePlayerTransferredNoticeDto.getBuyerTeamId();

        playerFacade.changePlayersTeam(playerId, newTeamId);
    }

    @KafkaListener(topics = "TeamDeleted")
    public void listenDeleted(ConsumerRecord<String, UUID> kafkaMessage) {
        final UUID teamId = kafkaMessage.value();
        playerFacade.deletePlayersAndTeam(teamId);
    }

}
