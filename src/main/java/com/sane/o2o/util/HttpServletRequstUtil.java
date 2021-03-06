package com.sane.o2o.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequstUtil {
    public static int getInt(HttpServletRequest request, String key) {
        try {
            return Integer.decode(request.getParameter(key));

        } catch (Exception e) {
            return -1;
        }
    }

    public static long getLong(HttpServletRequest request, String key) {
        try {
            return Long.decode(request.getParameter(key));

        } catch (Exception e) {
            return -1L;
        }
    }

    public static Double getDouble(HttpServletRequest request, String key) {
        try {
            return Double.valueOf(request.getParameter(key)).doubleValue();

        } catch (Exception e) {
            return -1.0;
        }
    }

    public static Boolean getBoolean(HttpServletRequest request, String key) {
        try {
            return Boolean.valueOf(request.getParameter(key));

        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    public static String getString(HttpServletRequest request, String key) {
        try {

            String result = request.getParameter(key);
            if (StringUtils.isNotEmpty(result)) {
                result = result.trim();
            }
            return result;

        } catch (Exception e) {
            return "";
        }
    }
}
