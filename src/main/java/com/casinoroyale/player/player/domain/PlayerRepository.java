package com.casinoroyale.player.player.domain;

import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

interface PlayerRepository extends JpaRepository<Player, UUID> {

    Stream<Player> findByTeamId(final UUID teamId);

}
