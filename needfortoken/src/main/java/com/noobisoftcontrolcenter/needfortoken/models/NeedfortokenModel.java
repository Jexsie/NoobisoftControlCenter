package com.noobisoftcontrolcenter.needfortoken.models;

import jakarta.persistence.*;

@Entity
public class NeedfortokenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    // Getters and setters
}




