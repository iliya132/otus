package ru.otus;

import org.flywaydb.core.Flyway;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.dao.ClientDao;
import ru.otus.dao.ClientDaoImpl;
import ru.otus.dao.InMemoryUserDao;
import ru.otus.dao.UserDao;
import ru.otus.models.Client;
import ru.otus.server.MyAppServer;
import ru.otus.server.TemplateProcessor;
import ru.otus.server.TemplateProcessorImpl;

import java.util.TimeZone;

public class WebServerApp {
    private static final String TEMPLATE_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "h2.cfg.xml";

    public static void main(String[] args) throws Exception {
        WebServerApp app = new WebServerApp();
        UserDao userDao = new InMemoryUserDao();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATE_DIR);
        ClientDao clientDao = app.initializeClientDao();
        MyAppServer server = new MyAppServer(userDao, templateProcessor, clientDao);
        server.start();
        server.join();
    }

    private ClientDao initializeClientDao() {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        executeMigrations(configuration);
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class);
        sessionFactory.withOptions().jdbcTimeZone(TimeZone.getTimeZone("UTC"));
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        return new ClientDaoImpl(transactionManager, clientTemplate);
    }

    private void executeMigrations(Configuration configuration) {
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        Flyway.configure()
                .dataSource(dbUrl, dbUserName, dbPassword)
                .locations("classpath:/migration")
                .load()
                .migrate();
    }
}