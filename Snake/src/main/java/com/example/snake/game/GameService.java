package com.example.snake.game;

import java.util.ArrayList;
import java.util.Random;

public class GameService {
    private static final int GRID_SIZE = 16;
    private final Random random = new Random();
    private GameState gameState;

    public void startGame() {
        gameState = new GameState();

        ArrayList<Position> snake = new ArrayList<>();

        snake.add(new Position(8, 8));

        gameState.setSnake(snake);

        gameState.setDirection(Direction.RIGHT);

        gameState.setScore(0);

        gameState.setGameOver(false);

        spawnApple();
    }

    public void move() {
        if (gameState.isGameOver()) {
            return;
        }

        Position head = gameState.getSnake().get(0);

        int x = head.getX();
        int y = head.getY();

        switch (gameState.getDirection()) {
            case UP -> y--;
            case DOWN -> y++;
            case LEFT -> x--;
            case RIGHT -> x++;
        }

        Position newHead = new Position(x, y);

        gameState.getSnake().add(0, newHead);

        if (x == gameState.getApple().getX() && y == gameState.getApple().getY()) {
            gameState.setScore(gameState.getScore() + 1);
            spawnApple();
        } else {
            gameState.getSnake().remove(gameState.getSnake().size() - 1);
        }
        checkCollision();
    }

    private void checkCollision() {
        Position head = getGameState().getSnake().get(0);

        int x = head.getX();
        int y = head.getY();

        if (x < 0 || y < 0 || x >= GRID_SIZE) {
            gameState.setGameOver(true);
            return;
        }

        for (int i = 1; i < gameState.getSnake().size(); i++) {

            Position body = gameState.getSnake().get(i);

            if (body.getX() == x && body.getY() == y) {

                gameState.setGameOver(true);

            }
        }
    }

    private void spawnApple() {

        int x = random.nextInt(GRID_SIZE);
        int y = random.nextInt(GRID_SIZE);

        gameState.setApple(new Position(x, y));

    }

    private void grow() {
    }

    public GameState getGameState() {
        return gameState;
    }

}
