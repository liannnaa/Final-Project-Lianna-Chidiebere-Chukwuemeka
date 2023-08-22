package com.company.gamestore.controller;

import com.company.gamestore.model.Game;
import com.company.gamestore.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Game addGame(@RequestBody Game game) {
        return gameService.addGame(game);
    }

    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/{id}")
    public Game getGameById(@PathVariable Integer id) {
        return gameService.getGameById(id).orElseThrow(() -> new RuntimeException("Game not found"));
    }

    @GetMapping("/studio/{studio}")
    public List<Game> findByStudio(@PathVariable String studio) {
        return gameService.findByStudio(studio);
    }

    @GetMapping("/rating/{esrbRating}")
    public List<Game> findByEsrbRating(@PathVariable String esrbRating) {
        return gameService.findByEsrbRating(esrbRating);
    }

    @GetMapping("/title/{title}")
    public List<Game> findByTitle(@PathVariable String title) {
        return gameService.findByTitle(title);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable Integer id) {
        gameService.deleteGame(id);
    }
}
