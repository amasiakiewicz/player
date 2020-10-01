package com.casinoroyale.player.team.domain;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration.builder;
import static org.joda.money.CurrencyUnit.USD;

import java.math.BigDecimal;
import java.util.UUID;

import com.casinoroyale.team.team.dto.CreateTeamNoticeDto;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeamFacadeIT {

    @Autowired //SUT
    private TeamFacade teamFacade;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void shouldCreateTeam() {
        //given
        final UUID teamId = randomUUID();
        final CreateTeamNoticeDto createTeamNoticeDto = givenCreateTeamNoticeDto(teamId);

        //when
        teamFacade.createTeam(createTeamNoticeDto);

        //then
        assertThat(teamInDb(teamId))
                .usingRecursiveComparison(builder().withIgnoredFields("version", "createdDateTime").build())
                .isEqualTo(expectedTeam(teamId));
    }

    @Test
    void shouldDeleteTeam() {
        //given
        final UUID teamId = givenTeamInDb();

        //when
        teamFacade.deleteTeam(teamId);

        //then
        assertThat(teamId).satisfies(this::doesntExistInDb);
    }

    private void doesntExistInDb(final UUID teamId) {
        final boolean exists = teamRepository.existsById(teamId);
        assertThat(exists).isFalse();
    }

    private UUID givenTeamInDb() {
        final UUID teamId = randomUUID();
        final Team team = new Team(teamId);
        teamRepository.save(team);

        return teamId;
    }

    private CreateTeamNoticeDto givenCreateTeamNoticeDto(final UUID teamId) {
        return new CreateTeamNoticeDto(teamId, BigDecimal.valueOf(0.04), Money.of(USD, 123));
    }

    private Team expectedTeam(final UUID teamId) {
        return new Team(teamId);
    }

    private Team teamInDb(final UUID teamId) {
        return teamRepository
                .findById(teamId)
                .orElseThrow(IllegalStateException::new);
    }
}