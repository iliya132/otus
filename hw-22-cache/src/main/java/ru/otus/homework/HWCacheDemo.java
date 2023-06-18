package ru.otus.homework;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.DataTemplateJdbc;
import ru.otus.jdbc.mapper.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaDataImpl;

import javax.sql.DataSource;
import java.util.function.Function;


public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    public static void main(String[] args) {
        var hwDemo = new HWCacheDemo();

        hwDemo.demo();
        hwDemo.dbDemo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        HwListener<String, Integer> listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.put("1", 1);

        logger.info("getValue:{}", cache.get("1"));
        cache.remove("1");
        cache.removeListener(listener);
    }

    private void dbDemo() {
        // INITIALIZE
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        // ACT
        transactionRunner.doInTransaction((status) -> {
            var serviceClient = generateTestData(dbExecutor, transactionRunner);
            var cachedServiceClient = new DbServiceClientCachedImpl(serviceClient);

            // result is "raw load from db finished in 1861ms"
            measureGet("raw load from db", (id) ->
                    // .orElseThrow потому что не тема ДЗ - тут есть уверенность что такая сущность есть
                    cachedServiceClient.getClient(id).orElseThrow()
            );
            // result is "using cache finished in 869ms"
            measureGet("using cache", (id) ->
                    cachedServiceClient.getClient(id).orElseThrow()
            );
            return null;
        });
    }

    private DBServiceClient generateTestData(DbExecutor dbExecutor, TransactionRunner transactionRunner) {
        var entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); //реализация DataTemplate, универсальная

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        for (int i = 0; i < 1000; i++) {
            dbServiceClient.saveClient(new Client("test-client-name#" + i));
        }
        return dbServiceClient;
    }

    private void measureGet(String name, Function<Long, Client> function) {
        StopWatch watch = new StopWatch();
        watch.start();
        for (long i = 1L; i <= 1000; i++) {
            function.apply(i);
        }
        for (long i = 1L; i <= 1000; i++) {
            function.apply(i);
        }
        watch.stop();
        logger.info("{} finished in {}ms", name, watch.getTotalTimeMillis());
    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
