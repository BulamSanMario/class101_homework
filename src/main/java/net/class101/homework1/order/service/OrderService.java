package net.class101.homework1.order.service;

import org.springframework.stereotype.Service;

import net.class101.homework1.common.util.ExHashMap;

@Service
public interface OrderService {
    public ExHashMap selectProductList(ExHashMap paramMap) throws Exception;
    
    public ExHashMap selectProductInfo(ExHashMap paramMap) throws Exception;
}
