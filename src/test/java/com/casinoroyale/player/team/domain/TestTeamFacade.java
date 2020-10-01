package com.casinoroyale.player.team.domain;

import static java.util.UUID.randomUUID;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TestTeamFacade {

    private final TeamRepository teamRepository;

    TestTeamFacade(final TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public UUID givenTeamInDb() {
        final UUID teamId = randomUUID();
        final Team team = new Team(teamId);
        teamRepository.save(team);

        return teamId;
    }

    public boolean existsById(final UUID teamId) {
        return teamRepository.existsById(teamId);
    }
}
