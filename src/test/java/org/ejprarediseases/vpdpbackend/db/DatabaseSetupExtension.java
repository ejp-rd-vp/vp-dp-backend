package org.ejprarediseases.vpdpbackend.db;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DatabaseSetupExtension implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        TestPostgresqlContainer container = TestPostgresqlContainer.getInstance();
        container.start();
        updateDataSourceProps(container);
    }

    private void updateDataSourceProps(TestPostgresqlContainer container) {
        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
    }

}
