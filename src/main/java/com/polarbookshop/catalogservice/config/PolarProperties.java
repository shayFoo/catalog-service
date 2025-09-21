package com.polarbookshop.catalogservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Polar Bookshop application.
 *
 * @param greeting a greeting message
 */
@ConfigurationProperties(prefix = "polar")
public record PolarProperties(
        String greeting
) {
}
