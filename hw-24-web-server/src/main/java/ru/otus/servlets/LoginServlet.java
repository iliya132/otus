package ru.otus.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.server.TemplateProcessor;
import ru.otus.services.UserAuthService;

import java.io.IOException;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 30;
    private final TemplateProcessor templateProcessor;
    private final UserAuthService userAuthService;

    public LoginServlet(TemplateProcessor templateProcessor, UserAuthService userAuthService) {
        this.templateProcessor = templateProcessor;
        this.userAuthService = userAuthService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sendLoginPage(response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        if (userAuthService.authenticate(name, password)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect("/admin");
        } else {
            sendLoginPage(response, "Invalid credentials");
        }
    }

    private void sendLoginPage(HttpServletResponse response) throws IOException {
        sendLoginPage(response, "");
    }

    private void sendLoginPage(HttpServletResponse response, String error) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, Map.of("error", error)));
    }
}
