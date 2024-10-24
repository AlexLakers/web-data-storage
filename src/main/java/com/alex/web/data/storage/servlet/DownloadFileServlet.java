package com.alex.web.data.storage.servlet;

import com.alex.web.data.storage.dto.ReadAccountDto;
import com.alex.web.data.storage.service.FileInfoService;
import com.alex.web.data.storage.util.UrlConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

/**
 * This class is servlet.It processes the request from user by URL '/files/download'.
 *
 * @see UrlConst UrlConst
 */

@Log4j
@WebServlet(UrlConst.DOWNLOAD)
public class DownloadFileServlet extends HttpServlet {
    private static final int BUFFER_SIZE = 4096;
    private final FileInfoService fileInfoService = FileInfoService.getInstance();

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var fileName = req.getParameter("file");
        var folderName = ((ReadAccountDto) req.getSession().getAttribute("account")).getFolder();
        var maybeInputStream = fileInfoService.getFile(folderName, fileName);
        maybeInputStream.ifPresentOrElse(
                inputStream -> {
                    resp.setHeader("content-disposition", "attachment;filename=" + fileName);
                    resp.setContentType("application/octet-stream");
                    write(resp, inputStream, BUFFER_SIZE);
                    log.info("The file:{%s} is downloaded successfully".formatted(fileName));
                },
                () -> {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    log.error("The file:{%s} can not be downloaded".formatted(fileName));
                }
        );
    }

    @SneakyThrows
    private void write(HttpServletResponse resp, InputStream inputStream, int buffSize) {
        resp.setContentLength(inputStream.available());
        log.debug("Write the file to the client output stream");

        try (var writableByteChannel = Channels.newChannel(resp.getOutputStream());
             var readableByteChannel = Channels.newChannel(inputStream)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(buffSize);
            while (readableByteChannel.read(byteBuffer) > 0) {
                log.debug("The byte buffer is:{%d}".formatted(byteBuffer.capacity()));
                byteBuffer.flip();
                writableByteChannel.write(byteBuffer);
                byteBuffer.compact();
            }
        }
    }
}