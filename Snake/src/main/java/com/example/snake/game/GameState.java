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

    public List<Position> getSnake() {
        return snake;
    }

    public void setSnake(List<Position> snake) {
        this.snake = snake;
    }

    public Position getApple() {
        return apple;
    }

    public void setApple(Position apple) {
        this.apple = apple;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
