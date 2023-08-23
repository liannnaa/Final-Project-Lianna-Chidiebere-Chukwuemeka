package com.company.gamestore.service;

import com.company.gamestore.model.Game;
import com.company.gamestore.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
}