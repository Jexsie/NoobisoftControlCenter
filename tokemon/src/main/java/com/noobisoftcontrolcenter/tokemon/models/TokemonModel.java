package com.noobisoftcontrolcenter.tokemon.models;

import jakarta.persistence.*;

@Entity
public class TokemonModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    // Getters and setters
}
