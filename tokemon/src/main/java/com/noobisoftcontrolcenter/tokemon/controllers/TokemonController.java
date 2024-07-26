package com.noobisoftcontrolcenter.tokemon.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tokemon")
public class TokemonController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, NFT tokemon!";
    }
}

