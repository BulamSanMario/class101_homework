package net.class101.homework1.common.cartlist;


import org.springframework.stereotype.Component;

import net.class101.homework1.common.util.ExHashMap;

@Component
public class CartList {
    private ExHashMap cartMap = new ExHashMap();
    private int totalPrice;
    private boolean isShippingCost = true;
    private int ShippingCost = 5000;
    
    public void addKlassToCart(ExHashMap productMap) {
        String num = productMap.getString("num");
        productMap.put("orderQty", 1);
        isShippingCost = false;
        cartMap.put(num, productMap);
    }
    
    public void addKitToCart(ExHashMap productMap) {
        String num = productMap.getString("num");
        int addOrderQty = productMap.getInt("orderQty");
        int totalQty = 0;
        
        ExHashMap beforeOrderMap = cartMap.getMap(num);
        
        if (beforeOrderMap != null) {
            totalQty += beforeOrderMap.getInt("orderQty");
        }
        
        totalQty += addOrderQty;
        productMap.put("orderQty", totalQty);
        cartMap.put(num, productMap);
    }
    
    public int getOrderQtyByProductNum(String productNum) {
        int resultQty = 0;
        ExHashMap beforeOrderMap = cartMap.getMap(productNum);
        
        if (beforeOrderMap != null) {
            resultQty = beforeOrderMap.getInt("orderQty");
        }
        
        return resultQty;
    }
    
}
