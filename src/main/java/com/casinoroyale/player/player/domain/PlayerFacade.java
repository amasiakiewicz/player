package com.casinoroyale.player.player.domain;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;
import static lombok.AccessLevel.PACKAGE;

import java.util.UUID;

import com.casinoroyale.player.player.dto.CreatePlayerDto;
import com.casinoroyale.player.player.dto.CreatePlayerNoticeDto;
import com.casinoroyale.player.player.dto.PlayerCreatedQueryDto;
import com.casinoroyale.player.player.dto.PlayerQueryDto;
import com.casinoroyale.player.player.dto.UpdatePlayerDto;
import com.casinoroyale.player.team.domain.TeamFacade;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AllArgsConstructor(access = PACKAGE)
public class PlayerFacade {

    private static final String PLAYER_CREATED_TOPIC = "PlayerCreated";
    private static final String PLAYER_TEAM_CHANGED_TOPIC = "PlayerTeamChanged";
    private static final String PLAYER_DELETED_TOPIC = "PlayerDeleted";

    private final PlayerRepository playerRepository;

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    private final TeamFacade teamFacade;

    public Page<PlayerQueryDto> findPlayers(final Pageable pageable) {
        return playerRepository
                .findAll(pageable)
                .map(Player::toQueryDto);
    }

    public PlayerCreatedQueryDto createPlayer(final CreatePlayerDto createPlayerDto) {
        checkArgument(createPlayerDto != null);

        final UUID teamId = createPlayerDto.getTeamId();
        checkTeamExists(teamId);

        final Player player = Player.create(createPlayerDto);
        playerRepository.save(player);

        final CreatePlayerNoticeDto createPlayerNoticeDto = player.toCreateNoticeDto();
        kafkaTemplate.send(PLAYER_CREATED_TOPIC, "", createPlayerNoticeDto);

        final UUID playerId = player.getId();
        return new PlayerCreatedQueryDto(playerId);
    }

    public void updatePlayer(final UUID playerId, final UpdatePlayerDto updatePlayerDto) {
        checkArgument(playerId != null);
        checkArgument(updatePlayerDto != null);

        final Player player = findPlayer(playerId);
        player.update(updatePlayerDto);
    }

    public void changePlayersTeam(final UUID playerId, final UUID newTeamId) {
        checkArgument(playerId != null);
        checkArgument(newTeamId != null);

        checkTeamExists(newTeamId);

        final Player player = findPlayer(playerId);
        player.changeTeam(newTeamId);

        kafkaTemplate.send(PLAYER_TEAM_CHANGED_TOPIC, playerId, newTeamId);
    }

    public void deletePlayer(final UUID playerId) {
        checkArgument(playerId != null);

        final Player player = findPlayer(playerId);
        deletePlayer(player);
    }

    public void deletePlayersAndTeam(final UUID teamId) {
        checkArgument(teamId != null);

        playerRepository
                .findByTeamId(teamId)
                .forEach(this::deletePlayer);

        teamFacade.deleteTeam(teamId);
    }

    private void deletePlayer(final Player player) {
        checkArgument(player != null);

        playerRepository.delete(player);

        final UUID playerId = player.getId();
        kafkaTemplate.send(PLAYER_DELETED_TOPIC, "", playerId);
    }

    private void checkTeamExists(final UUID teamId) {
        final boolean teamExists = teamFacade.teamExists(teamId);
        checkState(teamExists, format("Team %s doesn't exist", teamId));
    }

    private Player findPlayer(final UUID playerId) {
        return playerRepository
                .findById(playerId)
                .orElseThrow(() -> new IllegalStateException(format("Player %s doesn't exist", playerId)));
    }
}
