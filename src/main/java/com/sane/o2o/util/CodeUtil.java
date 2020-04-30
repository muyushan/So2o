package com.sane.o2o.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {

    public  static final String REGIST_SHOP_VERIFYCODE_KEY="registShopVerifyCode";
    public  static final String LOGIN_SYSTEN_VERIFYCODE_KEY="loginSystemVerifyCode";

    public static boolean verifyCode(String codeKey, String paramKey, HttpServletRequest request){
        String storedVerifyCode= (String) request.getSession().getAttribute(codeKey);
        String submitVerifyCode=HttpServletRequstUtil.getString(request,paramKey);
        return StringUtils.equals(storedVerifyCode.toUpperCase(),submitVerifyCode.toUpperCase());
    }
}
