package org.ejprarediseases.vpdpbackend.db;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestPostgresqlContainer extends PostgreSQLContainer<TestPostgresqlContainer> {

    private static final String IMAGE_VERSION = "postgres:15-alpine";
    private static final String DATABASE_NAME = "ejp";
    private static final String USERNAME = "ejp";
    private static final String PASSWORD = "ejp";

    private static TestPostgresqlContainer container;

    public static TestPostgresqlContainer getInstance() {
        if (container == null) {
            container = new TestPostgresqlContainer()
                    .withDatabaseName(DATABASE_NAME)
                    .withUsername(USERNAME)
                    .withPassword(PASSWORD);
        }
        return container;
    }

    public TestPostgresqlContainer() {
        super(IMAGE_VERSION);
    }
}

