package com.casinoroyale.player.team.domain;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

import java.util.UUID;

import com.casinoroyale.team.team.dto.CreateTeamNoticeDto;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AllArgsConstructor
public class TeamFacade {

    private final TeamRepository teamRepository;

    public void createTeam(final CreateTeamNoticeDto createTeamNoticeDto) {
        checkArgument(createTeamNoticeDto != null);

        final Team team = Team.create(createTeamNoticeDto);
        teamRepository.save(team);
    }

    public void deleteTeam(final UUID teamId) {
        checkArgument(teamId != null);

        final Team team = teamRepository
                .findById(teamId)
                .orElseThrow(() -> new IllegalStateException(format("Team %s doesn't exist", teamId)));

        teamRepository.delete(team);
    }

    public boolean teamExists(final UUID teamId) {
        return teamRepository.existsById(teamId);
    }
}
