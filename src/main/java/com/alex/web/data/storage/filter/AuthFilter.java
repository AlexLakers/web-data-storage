package com.alex.web.data.storage.filter;


import com.alex.web.data.storage.util.UrlConst;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * This class describes some filter that allows to check all the request by URL-pattern '/*'.
 * If URL is public or http client is login(HTTP Session has account data) then access is allowed.
 */

@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final List<String> publicPages = List.of(UrlConst.MAIN,UrlConst.REGISTER,UrlConst.LOGIN);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var uri=((HttpServletRequest)request).getRequestURI();
        if(isAccountLogin(request)||isPublicPage(uri)) {
            chain.doFilter(request, response);
        }
        else{
            var prevPage=((HttpServletRequest)request).getHeader("referer");
            ((HttpServletResponse)response).sendRedirect(prevPage!=null?prevPage:UrlConst.MAIN);
        }
    }

    private boolean isPublicPage(String url){
        if(url.equals(UrlConst.MAIN)){
            return true;
        }
        else {
            return publicPages.stream()
                    .filter(url1->!url1.equals(UrlConst.MAIN))
                    .anyMatch(url::startsWith);
        }
    }

    private boolean isAccountLogin(ServletRequest req){
        return ((((HttpServletRequest)req).getSession().getAttribute("account"))!=null);
    }
}
