package demo;

import core.repository.DataTemplateHibernate;
import core.repository.HibernateUtils;
import core.sessionmanager.TransactionManagerHibernate;
import crm.dbmigrations.MigrationsExecutorFlyway;
import crm.model.Address;
import crm.model.Client;
import crm.model.Phone;
import crm.service.DbServiceClientImpl;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        dbServiceClient.saveClient(new Client(null, "Vasya", new Address(null, "Mohovay street, 1"),
                List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333"))));

        var clientSecond = dbServiceClient.saveClient(new Client(null, "Petya", new Address(null, "Pyatnickaya street, 1"),
                List.of(new Phone(null, "55-111-66"), new Phone(null, "66-116-113"))));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}
