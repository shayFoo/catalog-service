package com.polarbookshop.catalogservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Polar Bookshop application.
 *
 */
@ConfigurationProperties(prefix = "polar")
public class PolarProperties {
    private String greeting;

    public String greeting() {
        return greeting;
    }
    
    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
