package com.alex.web.data.storage.servlet;

import com.alex.web.data.storage.util.UrlConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j;

import java.io.IOException;
/**
 * This class is servlet.It processes the request from user by URL '/logout'.
 * @see UrlConst UrlConst
 */

//@Slf4j
@Log4j
@WebServlet(UrlConst.LOGOUT)
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("The account:{%s} is logged out".formatted(req.getSession().getAttribute("account")));
        req.getSession().invalidate();
        resp.sendRedirect(UrlConst.MAIN);
    }
}