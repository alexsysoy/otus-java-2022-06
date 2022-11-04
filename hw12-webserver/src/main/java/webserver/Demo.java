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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import webserver.helpers.FileSystemHelper;
import webserver.server.ClientWebServer;
import webserver.server.ClientWebServerWithBasicSecurity;
import webserver.services.TemplateProcessor;
import webserver.services.TemplateProcessorImpl;

public class Demo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "WebServerRealm";

    public static void main(String[] args) throws Exception {
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);

        ClientWebServer clientWebServer = new ClientWebServerWithBasicSecurity(loginService, WEB_SERVER_PORT, templateProcessor);

        clientWebServer.start();
        clientWebServer.join();
    }
}
