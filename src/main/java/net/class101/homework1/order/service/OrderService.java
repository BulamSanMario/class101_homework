package net.class101.homework1.order.service;

import org.springframework.stereotype.Service;

import net.class101.homework1.common.util.ExHashMap;

@Service
public interface OrderService {
    public ExHashMap selectProductList() throws Exception;
    
    public ExHashMap selectProductInfo(ExHashMap paramMap) throws Exception;
    
    public int updateProductQty(ExHashMap paramMap) throws Exception;
}
