package net.class101.homework1.order.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.class101.homework1.common.exception.SoldOutException;
import net.class101.homework1.common.mapper.CommonMapper;
import net.class101.homework1.common.util.Const;
import net.class101.homework1.common.util.ExHashMap;
import net.class101.homework1.order.service.OrderService;

@Repository
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    CommonMapper commonMapper;
    
    //상품 목록
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ExHashMap selectProductList(ExHashMap paramMap) throws Exception {
        ExHashMap result = new ExHashMap();
        List resultList = new ArrayList();
        
        List list = commonMapper.getList(paramMap, "Order.selectProductList");
        
        //상품 정보 조합
        for (Object item : list) {
            ExHashMap itemMap = ExHashMap.newInstance((Map) item);
            StringBuffer sb = new StringBuffer();
            sb.append(itemMap.getString("num")).append("    ");
            sb.append(itemMap.getString("title")).append("    ");
            sb.append(itemMap.getString("price")).append("    ");
            sb.append(itemMap.getString("qty"));
            
            resultList.add(sb.toString());
        }
        
        result.put("list", resultList);
        
        return result;
    }
    
    //선택 상품 정보
    public ExHashMap selectProductInfo(ExHashMap paramMap) throws Exception {
        ExHashMap result = (ExHashMap) commonMapper.get(paramMap, "Order.selectProductInfo");
        
        if (result != null) {
            result.putResultCode(Const.ERROR_CODE.SUCCESS);
            
            if (result.getInt("qty") <= 0) {
                //재고 없음
                result.putResultCode(Const.ERROR_CODE.NOT_EXIST_QTY);
            }
            
            
        } else {
            //상품 없음
            result = new ExHashMap();
            result.putResultCode(Const.ERROR_CODE.NOT_EXIST_PRODUCT);
        }
        
        return result;
    }
    
    @Transactional
    public int updateProductQty(ExHashMap paramMap) throws Exception {
        
        for (String key : paramMap.keySet()) {
            ExHashMap productMap = paramMap.getMap(key);
            
            //클래스의 경우 재고가 없음
            if (productMap.getString("gbn").equals(Const.PRODUCT_TYPE.KIT.name())) {
                commonMapper.update(productMap, "Order.updateProductQty"); 
                int afterQty = (int) commonMapper.get(productMap, "Order.selectProductQty"); 
               
                if(afterQty < 0) {
                    throw new SoldOutException("재고가 부족합니다.");
                }
            }
        }
        
        return Const.ERROR_CODE.SUCCESS;
    }
}
