package net.class101.homework1.order.service.impl;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import net.class101.homework1.common.util.Const;
import net.class101.homework1.common.util.ExHashMap;

@Component
public class AsnycUpdateClass {
    
    CountDownLatch cl;
    
    @Autowired
    public OrderServiceImpl orderService;
    
    public ExHashMap testProductMap;
    
    public void setCountDownLatch(CountDownLatch cl) {
        this.cl = cl;
    }
    
    public void setTestProductMap(ExHashMap map) {
        this.testProductMap = map;
    }
    
    @Async
    public void asyncUpdate(int i) throws InterruptedException {
        try {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " ===================" + i + " 진행중");
            ExHashMap paramMap = new ExHashMap();
            paramMap.put(testProductMap.getString("num"), testProductMap);
            
            if (orderService.updateProductQty(paramMap) == Const.ERROR_CODE.SUCCESS) {
                System.out.println(threadName + " ===================" + i + " 완료");
            }
            
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            cl.countDown();
        }
    }
    
}
