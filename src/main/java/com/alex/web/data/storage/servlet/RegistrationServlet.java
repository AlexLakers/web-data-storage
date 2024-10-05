package com.alex.web.data.storage.servlet;

import com.alex.web.data.storage.dto.WriteAccountDto;
import com.alex.web.data.storage.entity.RoleName;
import com.alex.web.data.storage.exception.ServiceException;
import com.alex.web.data.storage.exception.ValidationException;
import com.alex.web.data.storage.util.JspConst;
import com.alex.web.data.storage.util.UrlConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j;

import java.io.IOException;


/**
 * This class is servlet.It processes the request from user by URL '/registration'.
 *
 * @see UrlConst UrlConst
 */

//@Slf4j
@Log4j
@WebServlet(UrlConst.REGISTER)
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var writeAccountDto = WriteAccountDto.builder()
                .firstName(req.getParameter("firstName"))
                .lastName(req.getParameter("lastName"))
                .birthDate(req.getParameter("birthDate"))
                .login(req.getParameter("login"))
                .password(req.getParameter("password"))
                .role(req.getParameter("role"))
                .folder(req.getParameter("folder"))
                .build();


        try {
        //call create account from service
            resp.sendRedirect(UrlConst.LOGIN);

    } catch (ValidationException e) {
        req.setAttribute("validErrors", e.getErrors());
        log.warn("The validation errors:{%s}".formatted(e.getErrors()));
        doGet(req, resp);
    } catch (ServiceException e) {
        log.error(" registration error:", e);
        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("roles", RoleName.values());
        req.getRequestDispatcher(JspConst.REGISTER).forward(req, resp);
    }
}