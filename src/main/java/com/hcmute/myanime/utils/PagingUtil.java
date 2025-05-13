package com.hcmute.myanime.utils;

public class PagingUtil {
    public static Boolean isNumber(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Boolean isValidPage(String page) {
        return page != null && !page.equals("") && isNumber(page) && Long.parseLong(page) >= 0;
    }
}
