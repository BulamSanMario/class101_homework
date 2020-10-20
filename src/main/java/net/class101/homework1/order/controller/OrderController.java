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
        
        System.out.print("입력(o[order]: 주문, q[quit]: 종료): ");
        inputValue = sc.next();
        
        switch (inputValue) {
        case "o": case "O":
            productList();
            selectProduct();
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
        ExHashMap paramMap = new ExHashMap();
        
        try {
            ExHashMap result = orderService.selectProductList(paramMap);
            List<String> productList = (List) result.get("list");

            System.out.println("상품번호       상품명           판매가격        재고수");
            
            for (String item : productList) {
                System.out.println(item);
            }

            selectProduct();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //상품 선택
    public void selectProduct() {
        ExHashMap paramMap = new ExHashMap();
        ExHashMap resultMap;
        int resultCode;
        
        try {
            System.out.print("상품번호 : ");
            
            inputValue = sc.next();
            
            //상품번호 숫자 체크
            if(!StringUtil.isNumeric(inputValue)) {
                System.out.println("상품번호를 다시 입력해 주세요.");
            }
            
            paramMap.put("num", inputValue);
            resultMap = orderService.selectProductInfo(paramMap);
            resultCode = resultMap.getInt("resultCode");
            
            //입력한 상품 확인
            switch (resultCode) {
                case Const.ERROR_CODE.NOT_EXIST_PRODUCT:
                    System.out.println("입력하신 상품번호의 상품이 없습니다. 다시 입력해 주세요.");
                    selectProduct();
                    
                    break;
                    
                case Const.ERROR_CODE.NOT_EXIST_QTY:
                    System.out.println("해당 상품 재고가 없습니다. 다시 입력해 주세요.");
                    selectProduct();
                    
                    break;
            }
            
            selectQuatity(resultMap);
            
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
            productList();
            
        } else {
        
            System.out.print("수량 : ");
            inputValue = sc.next();
            
            //상품번호 숫자 체크
            if(!StringUtil.isNumeric(inputValue)) {
                System.out.println("수량을 다시 입력해 주세요.");
                selectQuatity(productMap);
            }
            
            //재고보다 구매량이 많을 시
            int inputQty = Integer.parseInt(inputValue);
            int beforeQty = cartList.getOrderQtyByProductNum(productMap.getString("num"));
            
            //재고 수가 없을 시
            if(beforeQty >= hasQty) {
                System.out.println("재고 수량 : " + hasQty + " / 카트에 담긴 주문 수 : " + beforeQty);
                System.out.println("재고 수량이 부족합니다. 다른 상품을 선택해 주세요.");
                selectProduct();
            }
            
            //재고 보다 많이 주문 시
            if(inputQty + beforeQty > hasQty) {
                System.out.println("재고 수량 : " + hasQty + " / 카트에 담긴 주문 수 : " + beforeQty + " / 현재 주문 수 : " + inputQty);
                System.out.println("재고 수량이 부족합니다. 다시 입력해주세요.");
                selectQuatity(productMap);
            }
            
            productMap.put("orderQty", inputValue);
            cartList.addKitToCart(productMap);
            selectProduct();
            
        }
    }
    
    public void payment() {
        
    }
}