package com.company.gamestore.service;

import com.company.gamestore.model.Game;
import com.company.gamestore.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GameService {

    private GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game addGame(Game game) {
        return gameRepository.save(game);
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Optional<Game> getGameById(int id) {
        return gameRepository.findById(id);
    }

    public List<Game> findByStudio(String studio) {
        return gameRepository.findByStudio(studio);
    }

    public List<Game> findByEsrbRating(String esrbRating) {
        return gameRepository.findByEsrbRating(esrbRating);
    }

    public List<Game> findByTitle(String title) {
        return gameRepository.findByTitle(title);
    }

    public Game updateGame(Game game) {
        return gameRepository.save(game);
    }

    public void deleteGame(int id) {
        gameRepository.deleteById(id);
    }
}
