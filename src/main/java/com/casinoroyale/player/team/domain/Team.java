package com.casinoroyale.player.team.domain;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PRIVATE;

import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.Entity;

import com.casinoroyale.player.infrastructure.BaseEntity;
import com.casinoroyale.team.team.dto.CreateTeamNoticeDto;
import lombok.NoArgsConstructor;

@Entity
@Access(FIELD)
@NoArgsConstructor(access = PRIVATE)
class Team extends BaseEntity {

    static Team create(final CreateTeamNoticeDto createTeamNoticeDto) {
        final UUID teamId = createTeamNoticeDto.getTeamId();
        return new Team(teamId);
    }

    Team(final UUID id) {
        super(id);
    }

}
