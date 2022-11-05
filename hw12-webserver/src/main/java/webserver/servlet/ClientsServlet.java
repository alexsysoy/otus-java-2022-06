package webserver.servlet;

import db.crm.model.Address;
import db.crm.model.Client;
import db.crm.service.DBServiceClient;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import webserver.services.TemplateProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";

    private final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("clients", dbServiceClient.findAll());

        resp.setContentType("text/html");
        resp.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        String name = req.getParameter("name");
        String address = req.getParameter("address");

        Client client = dbServiceClient.saveClient(new Client(null, name, new Address(null, address), new ArrayList<>()));
        log.info("Added new client: {}", client);

        resp.sendRedirect(req.getContextPath() + "/clients");
    }
}
