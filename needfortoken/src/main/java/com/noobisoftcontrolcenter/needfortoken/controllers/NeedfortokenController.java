package com.noobisoftcontrolcenter.needfortoken.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/needfortoken")
public class NeedfortokenController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, NFT needfortoken!";
    }
}
