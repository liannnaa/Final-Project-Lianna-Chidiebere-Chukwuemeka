package com.company.gamestore.service;

import com.company.gamestore.exception.NotFoundException;
import com.company.gamestore.exception.InvalidException;
import com.company.gamestore.model.Game;
import com.company.gamestore.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game addGame(Game game) {
        if (game.getTitle() == null || game.getEsrbRating() == null || game.getDescription() == null
                || game.getStudio() == null || game.getPrice() == null) {
            throw new InvalidException("All game fields must be filled.");
        }
        return gameRepository.save(game);
    }

    public List<Game> getAllGames() {
        List<Game> games = gameRepository.findAll();
        if (games == null || games.isEmpty()) {
            throw new NotFoundException("No games found.");
        }
        return games;
    }

    public Optional<Game> getGameById(int id) {
        if (!gameRepository.existsById(id)) {
            throw new NotFoundException("Game not found with id: " + id);
        }
        return gameRepository.findById(id);
    }

    public List<Game> findByStudio(String studio) {
        List<Game> gamesByStudio = gameRepository.findByStudio(studio);
        if (gamesByStudio == null || gamesByStudio.isEmpty()) {
            throw new NotFoundException("No games found with studio: " + studio);
        }
        return gamesByStudio;
    }

    public List<Game> findByEsrbRating(String esrbRating) {
        List<Game> gamesByRating = gameRepository.findByEsrbRating(esrbRating);
        if (gamesByRating == null || gamesByRating.isEmpty()) {
            throw new NotFoundException("No games found with ESRB rating: " + esrbRating);
        }
        return gamesByRating;
    }

    public List<Game> findByTitle(String title) {
        return gameRepository.findByTitle(title);
    }

    public Game updateGame(Game game) {
        if (!gameRepository.existsById(game.getGameId())) {
            throw new NotFoundException("Game not found with id: " + game.getGameId());
        }
        if (game.getTitle() == null || game.getEsrbRating() == null || game.getDescription() == null
                || game.getStudio() == null || game.getPrice() == null) {
            throw new InvalidException("All game fields must be filled.");
        }
        return gameRepository.save(game);
    }

    public void deleteGame(int id) {
        if (!gameRepository.existsById(id)) {
            throw new NotFoundException("Game not found with id: " + id);
        }
        gameRepository.deleteById(id);
    }
}
