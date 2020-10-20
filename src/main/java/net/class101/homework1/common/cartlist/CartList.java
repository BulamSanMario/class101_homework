package net.class101.homework1.common.cartlist;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import net.class101.homework1.common.util.ExHashMap;

@Component
public class CartList {
    private ExHashMap cartMap = new ExHashMap();
    private int totalPrice;
    private boolean isShippingCost = true;
    private final int SHIPPING_COST = 5000;
    List<String> orderInfoArray;
    
    public void init() {
        this.cartMap.clear();
        this.totalPrice = 0;
        this.isShippingCost = true;
        this.orderInfoArray = new ArrayList<String>();
    }
    
    //클래스 추가
    public void addKlassToCart(ExHashMap productMap) {
        String num = productMap.getString("num");
        productMap.put("orderQty", 1);
        isShippingCost = false;
        cartMap.put(num, productMap);
    }
    
    //키트 추가
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
    
    //기존 주문 수량
    public int getOrderQtyByProductNum(String productNum) {
        int resultQty = 0;
        ExHashMap beforeOrderMap = cartMap.getMap(productNum);
        
        if (beforeOrderMap != null) {
            resultQty = beforeOrderMap.getInt("orderQty");
        }
        
        return resultQty;
    }
    
    //토탈 금액
    public int getTotalPrice() {
        
        for (String key : cartMap.keySet()) {
            ExHashMap productMap = cartMap.getMap(key);
            int price = productMap.getInt("price");
            int orderQty = productMap.getInt("orderQty");
            
            totalPrice += price * orderQty;
        }
        
        if (totalPrice >= 50000) {
            isShippingCost = false;
        }
        
        return totalPrice;
    }
    
    //카트에 담긴 상품
    public ExHashMap getCartMap() {
        return cartMap;
    }
    
    //배송비 필요 여부
    public boolean isShippingCost() {
        return isShippingCost;
    }
    
    //배송료
    public int getShippingCost() {
        return SHIPPING_COST;
    }
    
    //주문 정보
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List getOrderInfoArray() {
        
        for (String key : cartMap.keySet()) {
            ExHashMap productMap = cartMap.getMap(key);
            String title = productMap.getString("title");
            int orderQty = productMap.getInt("orderQty");
            
            String orderInfo = title + " - " + orderQty + "개";
            orderInfoArray.add(orderInfo);
        }
        
        return orderInfoArray;
    }
}
