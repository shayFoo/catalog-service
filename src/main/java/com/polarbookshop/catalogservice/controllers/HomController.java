package com.polarbookshop.catalogservice.controllers;

import com.polarbookshop.catalogservice.config.PolarProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomController {
    private final PolarProperties polarProperties;

    public HomController(PolarProperties polarProperties) {
        this.polarProperties = polarProperties;
    }


    @GetMapping("/")
    public String greeting() {
        return polarProperties.greeting();
    }
}
