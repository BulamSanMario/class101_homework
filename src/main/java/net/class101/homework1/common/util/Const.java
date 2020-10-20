package net.class101.homework1.common.util;

public class Const {
    
    public enum PRODUCT_TYPE {
        KIT,
        KLASS
    }
    
    /**
     * Error code
     */
    public class ERROR_CODE {
        /**
         * 성공
         */
        public static final int SUCCESS = 0;
        
        /**
         * 일반 에러
         */
        public static final int FAIL = -1;
        
        /**
         * 잘못된 parameter.
         */
        public static final int INVALID_PARAM = -2;
        
        /**
         * 상품이 없음.
         */
        public static final int NOT_EXIST_PRODUCT = -3;
        
        /**
         * 재고가 없음.
         */
        public static final int NOT_EXIST_QTY = -4;
    }
    
}
