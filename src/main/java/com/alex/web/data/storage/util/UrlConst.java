package com.alex.web.data.storage.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class contains all the URL for the mapping process between URL and servlet.
 */

@Getter
@AllArgsConstructor
public final class UrlConst {
    public static final String FILES="/files";
    public static final String MAIN="/";
    public static final String LOGIN="/login";
    public static final String LOGOUT="/logout";
    public static final String REGISTER="/registration";
    public static final String UPLOAD="/files/upload";
    public static final String DOWNLOAD="/files/download";
    public static final String DELETE="/files/delete";
}
