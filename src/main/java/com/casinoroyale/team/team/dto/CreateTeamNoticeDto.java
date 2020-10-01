package com.casinoroyale.team.team.dto;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigDecimal;
import java.util.UUID;

import com.casinoroyale.player.infrastructure.MoneyDeserializer;
import com.casinoroyale.player.infrastructure.MoneySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamNoticeDto {

    UUID teamId;

    BigDecimal commissionRate;

    @JsonSerialize(using = MoneySerializer.class)
    @JsonDeserialize(using = MoneyDeserializer.class)
    Money funds;

    public UUID getTeamId() {
        checkArgument(teamId != null);

        return teamId;
    }

    public BigDecimal getCommissionRate() {
        checkArgument(commissionRate != null);

        return commissionRate;
    }

    public Money getFunds() {
        checkArgument(funds != null);

        return funds;
    }

}
