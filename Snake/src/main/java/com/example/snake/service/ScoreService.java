package com.example.snake.service;

import com.example.snake.model.Score;
import com.example.snake.repository.ScoreRepository;
import com.example.snake.websocket.GameWebSocketHandler;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScoreService {

    private final ScoreRepository repository;
    private final GameWebSocketHandler socketHandler;

    public ScoreService(ScoreRepository repository,
                        GameWebSocketHandler socketHandler) {
        this.repository = repository;
        this.socketHandler = socketHandler;
    }

    public void saveScore(String name, int points) {
        Score score = new Score();
        score.setName(name);
        score.setPoints(points);

        repository.save(score);

        broadcastLeaderboard();
    }

    public List<Score> getTopScores() {
        return repository.findTop10ByOrderByPointsDesc();
    }

    public void broadcastLeaderboard() {
        try {
            List<Score> scores = getTopScores();

            socketHandler.broadcastRaw(
                    "LEADERBOARD",
                    scores
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}