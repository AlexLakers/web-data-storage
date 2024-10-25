package com.alex.web.data.storage.servlet;

import com.alex.web.data.storage.dto.ReadAccountDto;
import com.alex.web.data.storage.exception.ServiceException;
import com.alex.web.data.storage.service.AccountService;
import com.alex.web.data.storage.util.JspConst;
import com.alex.web.data.storage.util.UrlConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.util.Optional;

/**
 * This class is servlet.It processes the request from user by URL '/login'.
 *
 * @see UrlConst UrlConst
 */

@Log4j
@WebServlet(UrlConst.LOGIN)
public class LoginServlet extends HttpServlet {
    private static final String AUTH_WARN = "The authentication process is failed.Try again";
    private final AccountService accountService = AccountService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("fail") != null) {
            req.setAttribute("authWarn", AUTH_WARN);
        }
        req.getRequestDispatcher(JspConst.LOGIN).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var login = req.getParameter("login");
        var password = req.getParameter("password");

        Optional<ReadAccountDto> maybeLoginAccount = Optional.empty();
        try {
            maybeLoginAccount = accountService.login(login, password);
        } catch (ServiceException e) {
            log.error("Log in error:", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        maybeLoginAccount.ifPresentOrElse(
                (readAccountDto) -> {
                    loginSuccess(req, resp, readAccountDto);
                    log.info("The account:{%s} has been logged in successfully.".formatted(readAccountDto));
                },
                () -> {
                    loginFail(req, resp);
                    log.warn(AUTH_WARN);
                }
        );
    }

    @SneakyThrows
    private void loginFail(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("The handling process of login failed.");
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.sendRedirect(UrlConst.LOGIN.concat("?fail&login=" + req.getParameter("login")));
    }

    @SneakyThrows
    private void loginSuccess(HttpServletRequest req, HttpServletResponse resp, ReadAccountDto readAccountDto) {
        log.debug("The handling process of login success");
        req.getSession().setAttribute("account", readAccountDto);
        resp.sendRedirect(UrlConst.FILES);
    }
}