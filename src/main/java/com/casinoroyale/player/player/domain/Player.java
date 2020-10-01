package com.casinoroyale.player.player.domain;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.Entity;

import com.casinoroyale.player.infrastructure.BaseEntity;
import com.casinoroyale.player.player.dto.CreatePlayerDto;
import com.casinoroyale.player.player.dto.CreatePlayerNoticeDto;
import com.casinoroyale.player.player.dto.PlayerQueryDto;
import com.casinoroyale.player.player.dto.UpdatePlayerDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Access(FIELD)
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@ToString
class Player extends BaseEntity {

    private String name;

    private int number;

    private UUID teamId;

    private LocalDate dateOfBirth;

    private LocalDate playStart;

    private String college;

    static Player create(final CreatePlayerDto createPlayerDto) {
        return new Player(
                createPlayerDto.getName(),
                createPlayerDto.getNumber(),
                createPlayerDto.getTeamId(),
                createPlayerDto.getDateOfBirth(),
                createPlayerDto.getPlayStart(),
                createPlayerDto.getCollege()
        );
    }

    PlayerQueryDto toQueryDto() {
        final UUID playerId = getId();
        return new PlayerQueryDto(playerId, name, number, college, teamId, dateOfBirth, playStart);
    }

    CreatePlayerNoticeDto toCreateNoticeDto() {
        final UUID playerId = getId();
        return new CreatePlayerNoticeDto(playerId, teamId, dateOfBirth, playStart);
    }

    void update(final UpdatePlayerDto updatePlayerDto) {
        college = updatePlayerDto.getCollege();
        number = updatePlayerDto.getNumber();
    }

    void changeTeam(final UUID newTeamId) {
        teamId = newTeamId;
    }
}
