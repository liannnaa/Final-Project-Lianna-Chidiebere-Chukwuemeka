package com.company.gamestore.service;

import com.company.gamestore.exception.InvalidException;
import com.company.gamestore.exception.NotFoundException;
import com.company.gamestore.model.Game;
import com.company.gamestore.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc(addFilters = false)
public class GameServiceTest {
    private GameService gameService;
    private GameRepository gameRepository;

    @BeforeEach
    public void setUp() throws Exception {
        gameRepository = mock(GameRepository.class);
        gameService = new GameService(gameRepository);

        Game game = new Game();
        game.setGameId(1);
        game.setTitle("Test Title");
        game.setEsrbRating("Test Rating");
        game.setDescription("Test Description");
        game.setPrice(1.00);
        game.setStudio("Test Studio");
        game.setQuantity(1);

        Game game2 = new Game();
        game2.setGameId(2);
        game2.setTitle("Test Title 2");
        game2.setEsrbRating("Test Rating 2");
        game2.setDescription("Test Description 2");
        game2.setPrice(2.00);
        game2.setStudio("Test Studio 2");
        game2.setQuantity(2);

        List<Game> gameList = new ArrayList<>();
        gameList.add(game);
        gameList.add(game2);

        doReturn(game).when(gameRepository).save(game);
        doReturn(game2).when(gameRepository).save(game2);

        doReturn(Optional.of(game)).when(gameRepository).findById(1);
        doReturn(Optional.of(game2)).when(gameRepository).findById(2);

        doReturn(true).when(gameRepository).existsById(1);
        doReturn(true).when(gameRepository).existsById(2);

        doReturn(gameList).when(gameRepository).findAll();

        doReturn(gameList).when(gameRepository).findByStudio(anyString());

        doReturn(gameList).when(gameRepository).findByEsrbRating(anyString());

        doReturn(gameList).when(gameRepository).findByTitle(anyString());
    }

    @Test
    public void testAddGame() {
        Game gameToAdd = new Game();
        gameToAdd.setTitle("Test Title");
        gameToAdd.setEsrbRating("Test Rating");
        gameToAdd.setDescription("Test Description");
        gameToAdd.setPrice(1.00);
        gameToAdd.setStudio("Test Studio");
        gameToAdd.setQuantity(1);

        Game mockSavedGame = new Game();
        mockSavedGame.setGameId(1);
        mockSavedGame.setTitle("Test Title");
        mockSavedGame.setEsrbRating("Test Rating");
        mockSavedGame.setDescription("Test Description");
        mockSavedGame.setPrice(1.00);
        mockSavedGame.setStudio("Test Studio");
        mockSavedGame.setQuantity(1);

        when(gameRepository.save(gameToAdd)).thenReturn(mockSavedGame);

        Game savedGame = gameService.addGame(gameToAdd);
        assertEquals("Test Title", savedGame.getTitle());
        assertEquals(1, savedGame.getGameId());
    }

    @Test
    public void testGetAllGames() {
        List<Game> games = gameService.getAllGames();
        assertEquals(2, games.size());
    }

    @Test
    public void testGetGameById() {
        Optional<Game> game = gameService.getGameById(1);
        assertEquals("Test Title", game.get().getTitle());
    }

    @Test
    public void testFindByStudio() {
        List<Game> studioGames = new ArrayList<>();
        studioGames.add(new Game());
        doReturn(studioGames).when(gameRepository).findByStudio("Test Studio");

        List<Game> gamesByStudio = gameService.findByStudio("Test Studio");
        assertEquals(1, gamesByStudio.size());
    }

    @Test
    public void testFindByEsrbRating() {
        List<Game> ratingGames = new ArrayList<>();
        ratingGames.add(new Game());
        doReturn(ratingGames).when(gameRepository).findByEsrbRating("Test Rating");

        List<Game> gamesByRating = gameService.findByEsrbRating("Test Rating");
        assertEquals(1, gamesByRating.size());
    }

    @Test
    public void testFindByTitle() {
        List<Game> titleGames = new ArrayList<>();
        titleGames.add(new Game());
        doReturn(titleGames).when(gameRepository).findByTitle("Test Title");

        List<Game> gamesByTitle = gameService.findByTitle("Test Title");
        assertEquals(1, gamesByTitle.size());
    }

    @Test
    public void testUpdateGame() {
        Game gameToUpdate = new Game();
        gameToUpdate.setGameId(1);
        gameToUpdate.setTitle("Updated Title");
        gameToUpdate.setEsrbRating("Test Rating");
        gameToUpdate.setDescription("Test Description");
        gameToUpdate.setPrice(1.00);
        gameToUpdate.setStudio("Test Studio");
        gameToUpdate.setQuantity(1);

        when(gameRepository.save(gameToUpdate)).thenReturn(gameToUpdate);

        Game updatedGame = gameService.updateGame(gameToUpdate);
        assertEquals("Updated Title", updatedGame.getTitle());
    }

    @Test
    public void testDeleteGame() {
        doNothing().when(gameRepository).deleteById(1);

        gameService.deleteGame(1);
        verify(gameRepository).deleteById(1);
    }

    @Test
    public void testAddGameMissingFields() {
        Game incompleteGame = new Game();
        incompleteGame.setTitle("Incomplete Game");

        Exception exception = assertThrows(InvalidException.class, () -> {
            gameService.addGame(incompleteGame);
        });

        assertEquals("All game fields must be filled.", exception.getMessage());
    }

    @Test
    public void testGetGameByInvalidId() {
        when(gameRepository.existsById(99)).thenReturn(false);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            gameService.getGameById(99);
        });

        assertEquals("Game not found with id: 99", exception.getMessage());
    }

    @Test
    public void testUpdateNonExistingGame() {
        Game game = new Game();
        game.setGameId(99);
        game.setTitle("Non Existing Game");
        game.setEsrbRating("Test Rating");
        game.setDescription("Test Description");
        game.setPrice(1.00);
        game.setStudio("Test Studio");
        game.setQuantity(1);

        when(gameRepository.existsById(99)).thenReturn(false);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            gameService.updateGame(game);
        });

        assertEquals("Game not found with id: 99", exception.getMessage());
    }

    @Test
    public void testDeleteNonExistingGame() {
        when(gameRepository.existsById(99)).thenReturn(false);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            gameService.deleteGame(99);
        });

        assertEquals("Game not found with id: 99", exception.getMessage());
    }

    @Test
    public void testAddGameWithNegativePrice() {
        Game gameWithNegativePrice = new Game();
        gameWithNegativePrice.setTitle("Test Title");
        gameWithNegativePrice.setEsrbRating("Test Rating");
        gameWithNegativePrice.setDescription("Test Description");
        gameWithNegativePrice.setPrice(-1.00);
        gameWithNegativePrice.setStudio("Test Studio");
        gameWithNegativePrice.setQuantity(1);

        InvalidException thrown = assertThrows(
                InvalidException.class,
                () -> gameService.addGame(gameWithNegativePrice),
                "Expected addGame to throw an exception, but it didn't"
        );

        assertEquals("Game price must be greater than 0.", thrown.getMessage());
    }

    @Test
    public void testUpdateGameWithNegativePrice() {
        Game gameToUpdateWithNegativePrice = new Game();
        gameToUpdateWithNegativePrice.setGameId(1);
        gameToUpdateWithNegativePrice.setTitle("Test Title");
        gameToUpdateWithNegativePrice.setEsrbRating("Test Rating");
        gameToUpdateWithNegativePrice.setDescription("Test Description");
        gameToUpdateWithNegativePrice.setPrice(-1.00);
        gameToUpdateWithNegativePrice.setStudio("Test Studio");
        gameToUpdateWithNegativePrice.setQuantity(1);

        when(gameRepository.existsById(gameToUpdateWithNegativePrice.getGameId())).thenReturn(true);

        InvalidException thrown = assertThrows(
                InvalidException.class,
                () -> gameService.updateGame(gameToUpdateWithNegativePrice),
                "Expected updateGame to throw an exception, but it didn't"
        );

        assertEquals("Game price must be greater than 0.", thrown.getMessage());
    }

}