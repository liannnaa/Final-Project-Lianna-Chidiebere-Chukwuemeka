package com.company.gamestore.controller;

import com.company.gamestore.model.Game;
import com.company.gamestore.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class GameGraphController {
    @Autowired
    GameRepository gameRepository;

    @QueryMapping
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @QueryMapping
    public Game getGameById(@Argument int id) {
        Optional<Game> returnVal = gameRepository.findById(id);
        return returnVal.orElse(null);
    }

    @QueryMapping
    public List<Game> getGamesByTitle(@Argument String title) {
        return gameRepository.findByTitle(title);
    }

    @QueryMapping
    public List<Game> getGamesByEsrbRating(@Argument String esrbRating) {
        return gameRepository.findByEsrbRating(esrbRating);
    }

    @QueryMapping
    public List<Game> getGamesByStudio(@Argument String studio) {
        return gameRepository.findByStudio(studio);
    }
}