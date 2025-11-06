package com.mottu.trackyard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public String msgBoasVindas(){
        return "Bem vindo à nossa API";
    }

}