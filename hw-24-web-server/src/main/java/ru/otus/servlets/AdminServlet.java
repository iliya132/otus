package ru.otus.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.dao.ClientDao;
import ru.otus.models.Client;
import ru.otus.server.TemplateProcessor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AdminServlet extends HttpServlet {
    private final TemplateProcessor templateProcessor;
    private final ClientDao clientDao;
    private static final String ADMIN_PAGE = "admin.html";
    private static final String NAME_PARAM = "name";

    public AdminServlet(TemplateProcessor templateProcessor, ClientDao clientDao) {
        this.templateProcessor = templateProcessor;
        this.clientDao = clientDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var clients = clientDao.getClients();
        var page = templateProcessor.getPage(ADMIN_PAGE, Map.of("clientsTable", clientsToHtmlTable(clients)));
        resp.setContentType("text/html");
        resp.getWriter().println(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter(NAME_PARAM);
        clientDao.createClient(name);
        resp.sendRedirect("/admin");
    }

    private String clientsToHtmlTable(List<Client> clients) {
        StringBuilder sb = new StringBuilder();
        sb.append("<style>table, th, td { border: 1px solid black; border-collapse: collapse;}</style>" +
                "<table><thead><tr><th>id</th><th>name</th><th>created_at</th></tr></thead><tbody>");
        clients.forEach(it -> sb.append("<tr><td>%s</td><td>%s</td><td>%s</td></tr>".formatted(
                it.getId(),
                sanitize(it.getName()), // Элементарный способ санитайзинга, допустимо только на учебном проекте
                it.getCreatedAt().toString())));
        sb.append("</tbody></table>");

        return sb.toString();
    }

    private String sanitize(String text) {
        // Не позволяем встраивать исполняемые скрипты и тп в наш html
        // '<script>alert('hello world!')</script>' -> 'alert('hello world!')'
        return text.replaceAll("<.*?>", "");
    }
}
