package com.example.snake.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Score {

    @Id
    private String username;

    private int highscore;
}
