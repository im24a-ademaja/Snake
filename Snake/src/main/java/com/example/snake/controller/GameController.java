package com.example.snake.controller;

import com.example.snake.game.GameService;
import com.example.snake.game.GameState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public void startGame() {
        gameService.startGame();
    }

    @PostMapping("/move")
    public void move() {
        gameService.move();
    }

    @GetMapping("/state")
    public GameState getState() {
        return gameService.getGameState();
    }
}
