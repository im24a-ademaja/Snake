package com.example.snake.game;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameState {

    private List<Position> snake;

    private Position apple;

    private Direction direction;

    private int score;

    private boolean gameOver;

    @Override
    public String toString() {
        return "" + apple.getX();
    }
}
