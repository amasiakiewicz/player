package com.casinoroyale.player.team.domain;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

interface TeamRepository extends JpaRepository<Team, UUID> {

}
