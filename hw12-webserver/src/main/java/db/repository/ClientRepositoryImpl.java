package db.repository;

import db.core.repository.DataTemplateHibernate;
import db.core.repository.HibernateUtils;
import db.core.sessionmanager.TransactionManagerHibernate;
import db.crm.dbmigrations.MigrationsExecutorFlyway;
import db.crm.model.Address;
import db.crm.model.Client;
import db.crm.model.Phone;
import db.crm.service.DBServiceClient;
import db.crm.service.DbServiceClientImpl;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ClientRepositoryImpl implements ClientRepository {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private final DBServiceClient dbServiceClient;

    public ClientRepositoryImpl() {
        dbServiceClient = createDBServiceClient();
    }

    @Override
    public Client saveClientInDb(Client client) {
        return dbServiceClient.saveClient(client);
    }

    @Override
    public List<Client> getAllClientFromDb() {
        return dbServiceClient.findAll();
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
}
