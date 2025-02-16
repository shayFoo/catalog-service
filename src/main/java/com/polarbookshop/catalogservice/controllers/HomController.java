package com.polarbookshop.catalogservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomController {

    @GetMapping("/")
    public String greeting() {
        return "Welcome to the Polar Bookshop!";
    }
}
