package com.example.snake.controller;

import com.example.snake.model.Score;
import com.example.snake.service.ScoreService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/score")
public class ScoreController {

    private final ScoreService service;

    public ScoreController(ScoreService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public void save(@RequestParam String name, @RequestParam int points) {
        service.saveScore(name, points);
    }

    @GetMapping("/top")
    public List<Score> top() {
        return service.getTopScores();
    }
}
