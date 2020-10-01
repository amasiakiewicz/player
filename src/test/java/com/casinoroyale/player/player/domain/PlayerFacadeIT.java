package com.casinoroyale.player.player.domain;

import static java.time.LocalDate.of;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration.builder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.casinoroyale.player.player.dto.CreatePlayerDto;
import com.casinoroyale.player.player.dto.PlayerCreatedQueryDto;
import com.casinoroyale.player.player.dto.UpdatePlayerDto;
import com.casinoroyale.player.team.domain.TestTeamFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PlayerFacadeIT {

    @Autowired //SUT
    private PlayerFacade playerFacade;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TestTeamFacade testTeamFacade;

    @Autowired
    private TestPlayerFacade testPlayerFacade;

    @Test
    void shouldCreatePlayer() {
        //given
        final String name = "Tim Smith";
        final String college = "Georgia";
        final int number = 24;
        final UUID teamId = givenTeamInDb();
        final LocalDate dateOfBirth = of(2003, 10, 15);
        final LocalDate playStart = of(2012, 3, 1);
        final CreatePlayerDto createPlayerDto = givenCreatePlayerDto(name, number, college, teamId, dateOfBirth, playStart);

        //when
        final PlayerCreatedQueryDto playerDto = playerFacade.createPlayer(createPlayerDto);

        //then
        assertThat(existingPlayerInDb(playerDto))
                .usingRecursiveComparison(builder().withIgnoredFields("id", "version", "createdDateTime").build())
                .isEqualTo(expectedPlayer(name, college, number, teamId, dateOfBirth, playStart));
    }

    @Test
    void shouldNotCreatePlayerWhenTeamDoesntExist() {
        //given
        final String name = "Tim Smith";
        final String college = "Georgia";
        final int number = 24;
        final UUID teamId = givenTeamNotInDb();
        final LocalDate dateOfBirth = of(2003, 10, 15);
        final LocalDate playStart = of(2012, 3, 1);
        final CreatePlayerDto createPlayerDto = givenCreatePlayerDto(name, number, college, teamId, dateOfBirth, playStart);

        //when
        final Throwable thrownException = catchThrowable(() -> playerFacade.createPlayer(createPlayerDto));

        //then
        assertThat(thrownException).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldUpdatePlayerWithOnlyProvidedFields() {
        //given
        final String name = "Tim Smith";
        final UUID teamId = givenTeamInDb();
        final LocalDate dateOfBirth = of(2003, 10, 15);
        final LocalDate playStart = of(2012, 3, 1);

        final String oldCollege = "oldCollege";
        final int oldNumber = 64;
        final UUID playerId = givenPlayerInDb(name, oldCollege, oldNumber, teamId, dateOfBirth, playStart);

        final String newCollege = "newCollege";
        final int newNumber = 15;
        final UpdatePlayerDto updatePlayerDto = new UpdatePlayerDto(newCollege, newNumber);

        //when
        playerFacade.updatePlayer(playerId, updatePlayerDto);

        //then
        assertThat(existingPlayerInDb(playerId))
                .usingRecursiveComparison(builder().withIgnoredFields("id", "version", "createdDateTime").build())
                .isEqualTo(expectedPlayer(name, newCollege, newNumber, teamId, dateOfBirth, playStart));
    }

    @Test
    void shouldOnlyChangePlayersTeam() {
        //given
        final String name = "Tim Smith";
        final UUID oldTeamId = givenTeamInDb();
        final LocalDate dateOfBirth = of(2003, 10, 15);
        final LocalDate playStart = of(2012, 3, 1);
        final String college = "college";
        final int number = 64;
        final UUID playerId = givenPlayerInDb(name, college, number, oldTeamId, dateOfBirth, playStart);

        final UUID newTeamId = givenTeamInDb();

        //when
        playerFacade.changePlayersTeam(playerId, newTeamId);

        //then
        assertThat(existingPlayerInDb(playerId))
                .usingRecursiveComparison(builder().withIgnoredFields("id", "version", "createdDateTime").build())
                .isEqualTo(expectedPlayer(name, college, number, newTeamId, dateOfBirth, playStart));
    }

    @Test
    void shouldNotChangePlayersTeamWhenNewTeamDoesntExist() {
        //given
        final UUID playerId = givenPlayerInDb();
        final UUID newTeamId = givenTeamNotInDb();

        //when
        final Throwable thrownException = catchThrowable(() -> playerFacade.changePlayersTeam(playerId, newTeamId));

        //then
        assertThat(thrownException).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldDeletePlayer() {
        //given
        final UUID playerId = givenPlayerInDb();

        //when
        playerFacade.deletePlayer(playerId);

        //then
        assertThat(playerId).satisfies(this::doesntExistInDb);
    }

    @Test
    void shouldDeletePlayersAndTeam() {
        //given
        final UUID teamId = givenTeamInDb();
        givenPlayerInDb(teamId);
        givenPlayerInDb(teamId);

        //when
        playerFacade.deletePlayersAndTeam(teamId);

        //then
        assertThat(teamId).satisfies(this::doesntExistInDbAndNoPlayersPresent);
    }

    private UUID givenPlayerInDb() {
        final UUID teamId = givenTeamInDb();
        return givenPlayerInDb(teamId);
    }

    private UUID givenPlayerInDb(final UUID teamId) {
        final LocalDate dateOfBirth = of(2003, 8, 11);
        final LocalDate playStart = of(2017, 3, 15);

        return givenPlayerInDb("name", "college", 12, teamId, dateOfBirth, playStart);
    }

    private void doesntExistInDbAndNoPlayersPresent(final UUID teamId) {
        final Optional<Player> player = testPlayerFacade.findAnyPlayerByTeamId(teamId);
        assertThat(player).isEmpty();

        final boolean teamExists = testTeamFacade.existsById(teamId);
        assertThat(teamExists).isFalse();
    }

    private void doesntExistInDb(final UUID playerId) {
        final boolean exists = playerRepository.existsById(playerId);
        assertThat(exists).isFalse();
    }

    private UUID givenPlayerInDb(
            final String name, final String college, final int number, final UUID teamId, final LocalDate dateOfBirth,
            final LocalDate playStart
    ) {
        final CreatePlayerDto createPlayerDto =
                givenCreatePlayerDto(name, number, college, teamId, dateOfBirth, playStart);
        final PlayerCreatedQueryDto playerCreatedQueryDto = playerFacade.createPlayer(createPlayerDto);

        return playerCreatedQueryDto.getPlayerId();
    }

    private UUID givenTeamNotInDb() {
        return randomUUID();
    }

    private UUID givenTeamInDb() {
        return testTeamFacade.givenTeamInDb();
    }

    private Player expectedPlayer(
            final String name, final String college, final int number, final UUID teamId, final LocalDate dateOfBirth,
            final LocalDate playStart
    ) {
        final CreatePlayerDto createPlayerDto = givenCreatePlayerDto(name, number, college, teamId, dateOfBirth, playStart);
        return Player.create(createPlayerDto);
    }

    private CreatePlayerDto givenCreatePlayerDto(
            final String name, final int number, final String college, final UUID teamId, final LocalDate dateOfBirth,
            final LocalDate playStart
    ) {
        final CreatePlayerDto createPlayerDto = new CreatePlayerDto();
        createPlayerDto.setName(name);
        createPlayerDto.setNumber(number);
        createPlayerDto.setCollege(college);
        createPlayerDto.setTeamId(teamId);
        createPlayerDto.setDateOfBirth(dateOfBirth);
        createPlayerDto.setPlayStart(playStart);

        return createPlayerDto;
    }

    private Player existingPlayerInDb(final PlayerCreatedQueryDto playerCreatedQueryDto) {
        final UUID playerId = playerCreatedQueryDto.getPlayerId();
        return existingPlayerInDb(playerId);
    }

    private Player existingPlayerInDb(final UUID playerId) {
        return playerRepository
                .findById(playerId)
                .orElseThrow(IllegalStateException::new);
    }
}