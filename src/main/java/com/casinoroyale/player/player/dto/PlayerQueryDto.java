package com.casinoroyale.player.player.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Value;

@Value
public class PlayerQueryDto {

    UUID playerId;

    String name;

    int number;

    String college;

    UUID teamId;

    LocalDate dateOfBirth;

    LocalDate playStart;

}
