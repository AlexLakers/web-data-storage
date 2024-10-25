package com.alex.web.data.storage.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This class describes some filter that allows to record info about request(URL with parameters) into the console.
 */

@WebFilter("/*")
@Log4j
public class LoggFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var req = (HttpServletRequest) request;
        var param = req.getParameterMap().entrySet().stream()
                .map((entry) -> entry.getKey().concat(" : " + Arrays.toString(entry.getValue())))
                .collect(Collectors.joining());
        log.trace("The request URI:{%1$s} with params:{%2$s}".formatted(req.getRequestURI(), param));

        chain.doFilter(request, response);
    }
}
