package com.example.snake.game;

import java.util.List;
import java.awt.Point;

public class GameState {
    private List<Point> snake;
    private Point apple;
    private Direction direction;
    private int score;
    private boolean gameOver;
}
