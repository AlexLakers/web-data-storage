package com.alex.web.data.storage.servlet;


import com.alex.web.data.storage.dto.DeleteFileInfoDto;
import com.alex.web.data.storage.dto.ReadAccountDto;
import com.alex.web.data.storage.exception.ServiceException;
import com.alex.web.data.storage.service.FileInfoService;
import com.alex.web.data.storage.service.FileInfoServiceFactory;
import com.alex.web.data.storage.util.UrlConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;

import java.io.IOException;

/**
 * This class is servlet.It processes the request from user by URL '/files/delete'.
 * @see UrlConst UrlConst
 */

@Log4j
@WebServlet(UrlConst.DELETE)
public class DeleteFileServlet extends HttpServlet {
    private final FileInfoService fileInfoService= FileInfoServiceFactory.getFileInfoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var fileName=req.getParameter("file");
        var folderName=((ReadAccountDto)req.getSession().getAttribute("account")).getFolder();
        var deleteFileInfoDto= DeleteFileInfoDto.builder()
                .name(fileName)
                .id(req.getParameter("id"))
                .folder(folderName)
                .build();

        try {
            if (fileInfoService.deleteFile(deleteFileInfoDto)) {
                log.info("The file:{%s} has been deleted.".formatted(fileName));
                resp.sendRedirect(UrlConst.FILES.concat("?status=%1$s&file=%2$s".formatted("deleted", fileName)));
            }
        }
        catch (ServiceException e) {
            log.error("File deleting error",e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
