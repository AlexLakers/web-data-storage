package com.alex.web.data.storage.servlet;


import com.alex.web.data.storage.dto.ReadAccountDto;
import com.alex.web.data.storage.dto.ReadFileInfoDto;
import com.alex.web.data.storage.dto.WriteFileInfoDto;
import com.alex.web.data.storage.entity.Account;
import com.alex.web.data.storage.exception.ServiceException;
import com.alex.web.data.storage.service.FileInfoService;
import com.alex.web.data.storage.service.FileInfoServiceFactory;
import com.alex.web.data.storage.util.JspConst;
import com.alex.web.data.storage.util.UrlConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.time.LocalDate;

/**
 * This class is servlet.It processes the request from user by URL '/files/upload'.
 *
 * @see UrlConst UrlConst
 */

@Log4j
@WebServlet(UrlConst.UPLOAD)
@MultipartConfig(fileSizeThreshold = 2048)
public class UploadFileServlet extends HttpServlet {
    private final FileInfoService fileInfoService = FileInfoServiceFactory.getFileInfoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String referer = req.getHeader("referer");
        String prevPage = referer != null
                ? referer
                : UrlConst.MAIN;
        req.setAttribute("prevPage", prevPage);
        req.getRequestDispatcher(JspConst.UPLOAD).forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var readAccountDto = (ReadAccountDto) req.getSession().getAttribute("account");
        var writeFileInfoDto = WriteFileInfoDto.builder()
                .part(req.getPart("file"))
                .account(Account.builder()
                        .id(readAccountDto.getId())
                        .folder(readAccountDto.getFolder())
                        .build())
                .uploadDate(LocalDate.now())
                .build();

        ReadFileInfoDto readFileInfoDto = null;
        try {
            readFileInfoDto = fileInfoService.uploadFile(writeFileInfoDto);
        } catch (ServiceException e) {
            log.error("File uploading error:", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        log.info("The file:{%1$s} is uploaded successfully:{%2$s}".formatted(writeFileInfoDto.getPart().getSubmittedFileName(), readFileInfoDto));
        var prevPage = buildPrevPage(req.getParameter("prevPage"), readFileInfoDto.getName());

        resp.sendRedirect(prevPage);
    }

    private String buildPrevPage(String referer, String paramFileName) {
        return (referer != null)
                ? referer.replaceAll("\\?.*", "")
                .concat("?status=%2$s&file=%1$s".formatted(paramFileName, "uploaded"))
                : UrlConst.MAIN;
    }
}