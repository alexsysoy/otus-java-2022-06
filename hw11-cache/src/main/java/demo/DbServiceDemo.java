package demo;

import core.repository.DataTemplateHibernate;
import core.repository.HibernateUtils;
import core.sessionmanager.TransactionManagerHibernate;
import crm.dbmigrations.MigrationsExecutorFlyway;
import crm.model.Address;
import crm.model.Client;
import crm.model.Phone;
import crm.service.DBServiceClient;
import crm.service.DbServiceClientImpl;
import crm.service.DbServiceClientWithCacheDecorator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


// -Xms256m -Xmx256m
@Slf4j
public class DbServiceDemo {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {

        var clients = createClients(1000);
        var dbServiceClient = createDBServiceClient();
        var dbCacheServiceClient = new DbServiceClientWithCacheDecorator(dbServiceClient);

        System.out.println("-----------------------------------------------------------------");
        System.out.println("Время выполнения с кэшем: " + runTestCase(dbCacheServiceClient, clients));
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Время выполнения без кэша: " + runTestCase(dbServiceClient, clients));
        System.out.println("-----------------------------------------------------------------");
    }

    private static List<Client> createClients(int count) {
        var result = new ArrayList<Client>(count);
        for (int i = 1; i <= count; i++) {
            result.add(new Client(null, "Client" + i, new Address(null, "Address, " + i),
                    List.of(new Phone(null, String.valueOf(100 + i)), new Phone(null, String.valueOf(101 + i)))));
        }
        return result;
    }

    private static DBServiceClient createDBServiceClient() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    private static long runTestCase(DBServiceClient serviceClient, List<Client> clients) {
        var stopwatch = StopWatch.createStarted();

        List<Long> identifiers = new ArrayList<>();

        for (Client client : clients) {
            identifiers.add(serviceClient.saveClient(client).getId());
        }

//        System.gc();

        for (Long id : identifiers) {
            serviceClient.getClient(id);
        }

        stopwatch.stop();
        return stopwatch.getTime(TimeUnit.MILLISECONDS);
    }
}
