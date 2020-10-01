package com.casinoroyale.player.player.dto;

import static lombok.AccessLevel.NONE;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CreatePlayerDto {

    @NotBlank(message = "name.required")
    private String name;

    @Valid
    @Getter(NONE)
    @Setter(NONE)
    private UpdatePlayerDto updatePlayerDto;

    @NotNull(message = "teamId.required")
    private UUID teamId;

    @NotNull(message = "dateOfBirth.required")
    @Past(message = "dateOfBirth.past")
    private LocalDate dateOfBirth;

    @NotNull(message = "playStart.required")
    @Past(message = "playStart.past")
    private LocalDate playStart;

    public CreatePlayerDto() {
        updatePlayerDto = new UpdatePlayerDto();
    }

    public String getCollege() {
        return updatePlayerDto.getCollege();
    }

    public void setCollege(final String college) {
        updatePlayerDto.setCollege(college);
    }

    public int getNumber() {
        return updatePlayerDto.getNumber();
    }

    public void setNumber(final int number) {
        updatePlayerDto.setNumber(number);
    }

}
