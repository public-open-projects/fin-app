package com.socrates.fin_app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.integration.jdbc.initialize-schema=always"
})
@ActiveProfiles("test")
class FinAppApplicationTests {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> {
            FinAppApplication.main(new String[]{"--spring.profiles.active=test"});
        });
    }

    @Test
    void applicationStartsWithDefaultProfile() {
        assertDoesNotThrow(() -> {
            FinAppApplication.main(new String[]{"--spring.profiles.active=test"});
        });
    }

    @Test
    void applicationStartsWithCustomPort() {
        assertDoesNotThrow(() -> {
            FinAppApplication.main(new String[]{"--spring.profiles.active=test", "--server.port=8081"});
        });
    }
}
