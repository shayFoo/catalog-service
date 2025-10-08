package com.polarbookshop.catalogservice.annotations;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * Custom annotation for integration tests in the Polar Bookshop application.
 * It combines the @SpringBootTest annotation with a random web environment.
 * This annotation can be used on test classes to indicate that they are integration tests
 * that require a Spring application context and a web environment.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public @interface IntegrationTest {
}
