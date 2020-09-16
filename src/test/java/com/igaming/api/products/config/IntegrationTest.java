package com.igaming.api.products.config;

import com.igaming.api.products.domain.Product;
import com.igaming.api.products.repository.ProductRepository;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import io.restassured.RestAssured;
import lombok.SneakyThrows;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = IntegrationTest.PostgreSQLConfiguration.class)
@Testcontainers
@ActiveProfiles("it")
public class IntegrationTest {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private Flyway flyway;
    @LocalServerPort
    private int port;

    @PostConstruct
    private void init() {
        RestAssured.port = port;
    }

    @BeforeEach
    public void setUp() {
        flyway.migrate();
        repository.deleteAll().block();
    }

    protected void saveProducts(Product... products) {
        repository.saveAll(Arrays.asList(products)).collectList().block();
    }

    @SneakyThrows
    protected String getClassPathFileContent(String resource) {
        return new String(
            Files.readAllBytes(
                Paths.get(getClass().getResource(resource).toURI())
            ),
            Charset.defaultCharset()
        );
    }

    @TestConfiguration
    public static class PostgreSQLConfiguration {

        private static final int POSTGRE_SQL_MAPPED_PORT = 5432;

        @Bean(initMethod = "start", destroyMethod = "stop")
        public PostgreSQLContainer<?> postgreSQLContainer() {
            return new PostgreSQLContainer<>("postgres:11")
                .withDatabaseName("products")
                .withUsername("postgres")
                .withPassword("password")
                .waitingFor(
                    Wait.forLogMessage(".*database system is ready to accept connections.*", 1)
                );
        }

        @Bean
        public ConnectionFactory connectionFactory(PostgreSQLContainer<?> container) {
            return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                    .host(container.getHost())
                    .port(container.getMappedPort(POSTGRE_SQL_MAPPED_PORT))
                    .database(container.getDatabaseName())
                    .username(container.getUsername())
                    .password(container.getPassword())
                    .build()
            );
        }

        @Bean
        public Flyway flyway(PostgreSQLContainer<?> container) {
            return Flyway.configure()
                .dataSource(
                    "jdbc:postgresql://" + container.getHost() + ":" + container.getMappedPort(POSTGRE_SQL_MAPPED_PORT) + "/" + container.getDatabaseName(),
                    container.getUsername(),
                    container.getPassword()
                ).load();
        }
    }
}
