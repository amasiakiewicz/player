package com.casinoroyale.player.player;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.UUID;

import javax.validation.Valid;

import com.casinoroyale.player.player.domain.PlayerFacade;
import com.casinoroyale.player.player.dto.CreatePlayerDto;
import com.casinoroyale.player.player.dto.PlayerCreatedQueryDto;
import com.casinoroyale.player.player.dto.PlayerQueryDto;
import com.casinoroyale.player.player.dto.UpdatePlayerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/players")
class PlayerCrudController {

    private final PlayerFacade playerFacade;

    PlayerCrudController(final PlayerFacade playerFacade) {
        this.playerFacade = playerFacade;
    }

    @GetMapping
    Page<PlayerQueryDto> findPlayers(@PageableDefault(sort = { "name" }) final Pageable pageable) {
        return playerFacade.findPlayers(pageable);
    }

    @ResponseStatus(CREATED)
    @PostMapping
    PlayerCreatedQueryDto createPlayer(@Valid @RequestBody final CreatePlayerDto createPlayerDto) {
        return playerFacade.createPlayer(createPlayerDto);
    }

    @PutMapping("/{playerId}")
    void updatePlayer(
            @PathVariable final UUID playerId,
            @Valid @RequestBody final UpdatePlayerDto updatePlayerDto
    ) {
        playerFacade.updatePlayer(playerId, updatePlayerDto);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{playerId}")
    void deletePlayer(@PathVariable final UUID playerId) {
        playerFacade.deletePlayer(playerId);
    }

}
