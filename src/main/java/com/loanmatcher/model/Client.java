package com.loanmatcher.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int creditScore;

    protected Client() {
    }

    public Client(String name, int creditScore) {
        this.name = name;
        this.creditScore = creditScore;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCreditScore() {
        return creditScore;
    }

}
