package com.example.snake.repository;

import java.util.List;

import com.example.snake.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findTop10ByOrderByPointsDesc();

}