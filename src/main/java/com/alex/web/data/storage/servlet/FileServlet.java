package com.alex.web.data.storage.servlet;

import com.alex.web.data.storage.dto.FileFilterDto;
import com.alex.web.data.storage.dto.ReadAccountDto;
import com.alex.web.data.storage.dto.ReadFileInfoDto;
import com.alex.web.data.storage.exception.ServiceException;
import com.alex.web.data.storage.exception.ValidationException;
import com.alex.web.data.storage.service.FileInfoService;
import com.alex.web.data.storage.util.JspConst;
import com.alex.web.data.storage.util.UrlConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.util.List;

import static com.alex.web.data.storage.servlet.FileServlet.FilterName.*;


/**
 * This class is servlet.It processes the request from user by URL '/files'.
 *
 * @see UrlConst UrlConst
 */

//@Slf4j
@Log4j
@WebServlet(UrlConst.FILES)
public class FileServlet extends HttpServlet {
    private final FileInfoService fileInfoService = FileInfoService.getInstance();

    @Getter
    @AllArgsConstructor
    enum FilterName {
        NAME("name"),
        SIZE("size"),
        UPLOAD_DATE("uploadDate"),
        LIMIT("limit");
        private final String value;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspConst.FILES).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var currentSession = req.getSession();
        var readAccountDto = (ReadAccountDto) currentSession.getAttribute("account");
        var fileFilterDto = FileFilterDto.builder()
                .name(req.getParameter(NAME.getValue()))
                .size(req.getParameter(SIZE.getValue()))
                .uploadDate(req.getParameter(UPLOAD_DATE.getValue()))
                .limit(req.getParameter(LIMIT.getValue()))
                .accountId(String.valueOf(readAccountDto.getId()))
                .build();

        List<ReadFileInfoDto> readFileInfoDtoList = null;
        try {
            readFileInfoDtoList = fileInfoService.findAll(fileFilterDto);
            log.info("All the founded files:{%1$s} by account:{%2$s} by filter:{%3$s}"
                    .formatted(readFileInfoDtoList, readAccountDto, fileFilterDto));

            req.setAttribute("readFileInfoDtoList", readFileInfoDtoList);
            doGet(req, resp);

        } catch (ValidationException e) {
            log.warn("The validation errors:{%s}".formatted(e.getErrors()));
            req.setAttribute("validErrors", e.getErrors());
            doGet(req, resp);

        } catch (ServiceException e) {
            log.error(e.getCause().getMessage());
            req.setAttribute("serviceError", e.getCause());
            doGet(req, resp);
        }
    }
}
