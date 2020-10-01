package com.casinoroyale.player.player.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlayerDto {

    @NotBlank(message = "college.required")
    private String college;

    @NotNull(message = "number.required")
    @Range(min = 1, max = 100, message = "number.range")
    private int number;

}
