package webserver;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница клиентов
    http://localhost:8080/index

    // Страница клиентов
    http://localhost:8080/clients

    Логин: admin
    Пароль: pass

 */

import db.core.repository.DataTemplateHibernate;
import db.core.repository.HibernateUtils;
import db.core.sessionmanager.TransactionManagerHibernate;
import db.crm.dbmigrations.MigrationsExecutorFlyway;
import db.crm.model.Address;
import db.crm.model.Client;
import db.crm.model.Phone;
import db.crm.service.DBServiceClient;
import db.crm.service.DbServiceClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.cfg.Configuration;
import webserver.helpers.FileSystemHelper;
import webserver.server.ClientWebServer;
import webserver.server.ClientWebServerWithBasicSecurity;
import webserver.services.TemplateProcessor;
import webserver.services.TemplateProcessorImpl;

@Slf4j
public class Demo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "WebServerRealm";

    public static void main(String[] args) throws Exception {
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);

        DBServiceClient dbServiceClient = createDBServiceClient();

        ClientWebServer clientWebServer = new ClientWebServerWithBasicSecurity(loginService, WEB_SERVER_PORT, templateProcessor, dbServiceClient);

        clientWebServer.start();
        clientWebServer.join();
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
