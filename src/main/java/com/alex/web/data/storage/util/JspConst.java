package com.alex.web.data.storage.util;

/**
 * This class contains all the paths to jsp-pages in the app.
 */

public class JspConst {
    private static final String BASE_PATH = "/WEB-INF/views/%1$s.jsp";
    public static final String FILES = BASE_PATH.formatted("files");
    public static final String HEADER = BASE_PATH.formatted("header");
    public static final String LOGIN = BASE_PATH.formatted("login");
    public static final String REGISTER = BASE_PATH.formatted("registration");
    public static final String UPLOAD = BASE_PATH.formatted("upload");
}