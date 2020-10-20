package net.class101.homework1.common.util;

import java.text.DecimalFormat;

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
    
    //문자열 공백 또는 빈문자열 체크
    public static boolean isEmptyString(String str) {
        return str == null || str.trim().length() == 0;
    }
    
    //원화로 표기
    public static String getDecimalKRW(int price) {
        String resultKRW;
        
        DecimalFormat df = new DecimalFormat("###,###");
        resultKRW = df.format(price) + "원";
        
        return resultKRW;
    }
}
