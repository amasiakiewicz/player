package com.casinoroyale.player.player.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
class TestPlayerFacade {

    private final PlayerRepository playerRepository;

    TestPlayerFacade(final PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Optional<Player> findAnyPlayerByTeamId(final UUID teamId) {
        return playerRepository
                .findByTeamId(teamId)
                .findAny();
    }

}
