package com.example.snake.game;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.ArrayList;
import java.util.Random;

@Service
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

    @Scheduled(fixedRate = 200)
    public void tick() {
        if (gameState == null || gameState.isGameOver()) {
            return;
        }
        move();
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

    public void setDirection(Direction newDirection) {

        if (gameState == null || gameState.isGameOver()) {
            return;
        }

        Direction current = gameState.getDirection();

        if ((current == Direction.UP && newDirection == Direction.DOWN) ||
                (current == Direction.DOWN && newDirection == Direction.UP) ||
                (current == Direction.LEFT && newDirection == Direction.RIGHT) ||
                (current == Direction.RIGHT && newDirection == Direction.LEFT)) {

            return;
        }

        gameState.setDirection(newDirection);
    }

    private void checkCollision() {
        Position head = getGameState().getSnake().get(0);

        int x = head.getX();
        int y = head.getY();

        if (x < 0 || y < 0 || x >= GRID_SIZE || y >= GRID_SIZE) {
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

        Position apple;

        do {
            int x = random.nextInt(GRID_SIZE);
            int y = random.nextInt(GRID_SIZE);
            apple = new Position(x, y);
        } while (gameState.getSnake().contains(apple));

        gameState.setApple(apple);

    }

    public GameState getGameState() {
        return gameState;
    }
}