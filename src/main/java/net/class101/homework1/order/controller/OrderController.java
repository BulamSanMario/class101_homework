package net.class101.homework1.order.controller;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import net.class101.homework1.common.cartlist.CartList;
import net.class101.homework1.common.util.Const;
import net.class101.homework1.common.util.ExHashMap;
import net.class101.homework1.common.util.StringUtil;
import net.class101.homework1.order.service.OrderService;

@Controller
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CartList cartList;
    
    //사용자 입력값
    private Scanner sc = new Scanner(System.in);
    private String inputValue;
    
    //주문 대기
    public void initOrder() {
        cartList.init();
        
        System.out.print("입력(o[order]: 주문, q[quit]: 종료): ");
        inputValue = sc.next();
        
        switch (inputValue) {
        case "o": case "O":
            productList();
            break;
            
        case "q": case "Q":
            sc.close();
            System.out.print("고객님의 주문 감사합니다.");
            break;

        default:
            System.out.println("잘못 입력하셨습니다.");
            initOrder();
            break;
        }
    }
    
    //상품 목록
    @SuppressWarnings({ "unchecked", "rawtypes", "unused" })
    public void productList() {
        
        try {
            ExHashMap result = orderService.selectProductList();
            List<String> productList = (List) result.get("list");

            System.out.println("상품번호       상품명           판매가격        재고수");
            
            for (String item : productList) {
                System.out.println(item);
            }
            
            selectProduct(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //상품 선택
    public void selectProduct(boolean isScannerNextLine) {
        ExHashMap paramMap = new ExHashMap();
        ExHashMap resultMap;
        int resultCode;
        
        try {
            //이전 입력값 제거용
            if(isScannerNextLine) {
                sc.nextLine();
            }
            
            System.out.print("상품번호 : ");
            inputValue = sc.nextLine();
            
            if (StringUtil.isEmptyString(inputValue)) {
                //결제
                payment();
                
            } else {
            
                //상품번호 숫자 체크
                if(!StringUtil.isNumeric(inputValue)) {
                    System.out.println("상품번호를 다시 입력해 주세요.");
                    selectProduct(false);
                } else {
                
                    paramMap.put("num", inputValue);
                    resultMap = orderService.selectProductInfo(paramMap);
                    resultCode = resultMap.getInt("resultCode");
                    
                    //입력한 상품 확인
                    switch (resultCode) {
                        case Const.ERROR_CODE.NOT_EXIST_PRODUCT:
                            System.out.println("입력하신 상품번호의 상품이 없습니다. 다시 입력해 주세요.");
                            selectProduct(false);
                            
                            break;
                            
                        case Const.ERROR_CODE.NOT_EXIST_QTY:
                            System.out.println("해당 상품 재고가 없습니다. 다시 입력해 주세요.");
                            selectProduct(false);
                            
                            break;
                            
                        default:
                            selectQuatity(resultMap);
                            break;
                    }
                    
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    //상품 수량 선택
    public void selectQuatity(ExHashMap productMap) {
        int hasQty = productMap.getInt("qty");
        String productType = productMap.getString("gbn");
        
        if(productType.equals(Const.PRODUCT_TYPE.KLASS.name())) {
            cartList.addKlassToCart(productMap);
            selectProduct(false);
            
        } else {
        
            System.out.print("수량 : ");
            inputValue = sc.next();
            
            //상품번호 숫자 체크
            if(!StringUtil.isNumeric(inputValue)) {
                System.out.println("수량을 다시 입력해 주세요.");
                selectQuatity(productMap);
            } else {
            
                //재고, 구매 수 체크
                int inputQty = Integer.parseInt(inputValue);
                int beforeQty = cartList.getOrderQtyByProductNum(productMap.getString("num"));
                
                if(beforeQty >= hasQty) {
                    //재고 수가 없을 시
                    System.out.println("재고 수량 : " + hasQty + " / 카트에 담긴 주문 수 : " + beforeQty);
                    System.out.println("재고 수량이 부족합니다. 다른 상품을 선택해 주세요.");
                    
                    selectProduct(true);
                } else if(inputQty + beforeQty > hasQty) {
                    //재고 보다 많이 주문 시
                    System.out.println("재고 수량 : " + hasQty + " / 카트에 담긴 주문 수 : " + beforeQty + " / 현재 주문 수 : " + inputQty);
                    System.out.println("재고 수량이 부족합니다. 다시 입력해주세요.");
                    selectQuatity(productMap);
                } else {
                    productMap.put("orderQty", inputValue);
                    cartList.addKitToCart(productMap);
                    
                    selectProduct(true);
                }
                
            }
            
        }
    }
    
    //결제 기능
    @SuppressWarnings("unchecked")
    public void payment() {
        ExHashMap cartMap = cartList.getCartMap();
        
        try {
            int updateResult = orderService.updateProductQty(cartMap);
            
            if (updateResult == Const.ERROR_CODE.SUCCESS) {
                int totalPrice = 0;
                int price = cartList.getTotalPrice();
                int shippingCost = cartList.getShippingCost();
                boolean isShippingCost = cartList.isShippingCost();
                List<String> orderInfoArray = cartList.getOrderInfoArray();
                
                System.out.println("주문 내역:");
                System.out.println("----------------------------------------------");
                for (String item : orderInfoArray) {
                    System.out.println(item);
                }
                System.out.println("----------------------------------------------");
                
                System.out.println("주문금액: " + StringUtil.getDecimalKRW(price));
                
                if(isShippingCost) {
                    System.out.println("배송비: " + StringUtil.getDecimalKRW(shippingCost));
                    totalPrice += shippingCost;
                }
                System.out.println("----------------------------------------------");
                
                totalPrice += price;
                
                System.out.println("지불금액: " + StringUtil.getDecimalKRW(totalPrice));
                System.out.println("----------------------------------------------");
                System.out.println("");
                
                initOrder();
                
            }
            
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}