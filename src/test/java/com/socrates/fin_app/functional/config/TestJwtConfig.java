package com.socrates.fin_app.functional.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestJwtConfig {
    // Configuration moved to DefaultIdpProvider
}
