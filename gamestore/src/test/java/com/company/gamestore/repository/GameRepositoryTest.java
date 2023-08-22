package com.company.gamestore.repository;

import com.company.gamestore.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameRepositoryTest {

    @Autowired
    GameRepository gameRepo;

    private Game game;

    @BeforeEach
    public void setUp() {
        gameRepo.deleteAll();

        // Arrange
        game = new Game();
        game.setTitle("Test Title");
        game.setEsrbRating("E");
        game.setDescription("Test Description");
        game.setPrice(59.99);
        game.setStudio("Test Studio");
        game.setQuantity(10);
        game = gameRepo.save(game);
    }

    @Test
    public void addGame() {
        // Assert
        Optional<Game> foundGame = gameRepo.findById(game.getGameId());
        assertEquals(foundGame.get(), game);
    }

    @Test
    public void updateGame() {
        // Act
        game.setTitle("UPDATED TITLE");
        gameRepo.save(game);

        // Assert
        Optional<Game> foundGame = gameRepo.findById(game.getGameId());
        assertEquals(foundGame.get(), game);
    }

    @Test
    public void deleteGame() {
        // Act
        gameRepo.deleteById(game.getGameId());

        // Assert
        Optional<Game> foundGame = gameRepo.findById(game.getGameId());
        assertFalse(foundGame.isPresent());
    }

    @Test
    public void getGameById() {
        // Act
        Game foundGame = gameRepo.findById(game.getGameId()).orElse(null);

        // Assert
        assertEquals(game, foundGame);
    }

    @Test
    public void getGamesByStudio() {
        // Arrange
        Game game2 = new Game();
        game2.setTitle("Test Title 2");
        game2.setEsrbRating("E");
        game2.setDescription("Test Description 2");
        game2.setPrice(69.99);
        game2.setStudio("Different Studio");
        game2.setQuantity(20);
        gameRepo.save(game2);

        // Act
        List<Game> games = gameRepo.findByStudio(game.getStudio());

        // Assert
        assertTrue(games.contains(game));
        assertFalse(games.contains(game2));
    }
}
