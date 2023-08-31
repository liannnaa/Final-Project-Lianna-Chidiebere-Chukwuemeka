package com.company.gamestore.repository;

import com.company.gamestore.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
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
        game.setEsrbRating("Test Rating");
        game.setDescription("Test Description");
        game.setPrice(1.00);
        game.setStudio("Test Studio");
        game.setQuantity(1);
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
        game2.setEsrbRating("Test Rating 2");
        game2.setDescription("Test Description 2");
        game2.setPrice(2.00);
        game2.setStudio("Test Studio 2");
        game2.setQuantity(2);
        gameRepo.save(game2);

        // Act
        List<Game> games = gameRepo.findByStudio(game.getStudio());

        // Assert
        assertTrue(games.contains(game));
        assertFalse(games.contains(game2));
    }

    @Test
    public void getGamesByEsrbRating() {
        // Arrange
        Game game2 = new Game();
        game2.setTitle("Test Title 2");
        game2.setEsrbRating("Test Rating 2");
        game2.setDescription("Test Description 2");
        game2.setPrice(2.00);
        game2.setStudio("Test Studio 2");
        game2.setQuantity(2);
        gameRepo.save(game2);

        // Act
        List<Game> games = gameRepo.findByEsrbRating(game.getEsrbRating());

        // Assert
        assertTrue(games.contains(game));
        assertFalse(games.contains(game2));
    }

    @Test
    public void getGamesByTitle() {
        // Arrange
        Game game2 = new Game();
        game2.setTitle("Test Title 2");
        game2.setEsrbRating("Test Rating 2");
        game2.setDescription("Test Description 2");
        game2.setPrice(2.00);
        game2.setStudio("Test Studio 2");
        game2.setQuantity(2);
        gameRepo.save(game2);

        // Act
        List<Game> games = gameRepo.findByTitle(game.getTitle());

        // Assert
        assertTrue(games.contains(game));
        assertFalse(games.contains(game2));
    }
}
