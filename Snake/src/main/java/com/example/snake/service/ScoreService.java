package com.example.snake.service;

import com.example.snake.model.Score;
import com.example.snake.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScoreService {

    private final ScoreRepository repo;

    public ScoreService(ScoreRepository repo) {
        this.repo = repo;
    }

    public void saveScore(String name, int points) {
        Score score = new Score();
        score.setName(name);
        score.setPoints(points);

        repo.save(score);
    }

    public List<Score> getTopScores() {
        return repo.findTop10ByOrderByPointsDesc();
    }
}