package net.class101.homework1.common.util;

public class StringUtil {
    
    //문자열 숫자 구분
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
