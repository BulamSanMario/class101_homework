package net.class101.homework1.order.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import net.class101.homework1.common.util.Const;
import net.class101.homework1.common.util.ExHashMap;

@SpringBootTest
@ActiveProfiles("test")
class MutilThreadReqTest {

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    AsnycUpdateClass asnycUpdateClass;
    
    @Test
    public void asyncTest() throws Exception {
        CountDownLatch cl = new CountDownLatch(10);
        boolean wellDone = false;

        ExHashMap productMap = new ExHashMap();
        
        //"58395", "KIT", "선과 여백으로 채우는 2020년 캘린더와 엽서", 18620, 31
        //재고 31개 제품
        productMap.put("gbn", Const.PRODUCT_TYPE.KIT.name());
        productMap.put("num", 58395);
        productMap.put("orderQty", 5);
        
        asnycUpdateClass.setCountDownLatch(cl);
        asnycUpdateClass.setTestProductMap(productMap);

        try {
            for (int i = 0; i < 10; i++) {
                asnycUpdateClass.asyncUpdate(i);
            }
            wellDone = cl.await(60, TimeUnit.SECONDS);
            System.out.println(String.format("메인스레드 종료 (timeout=%b)", !wellDone));
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        assertThat(equalTo(wellDone));
        
    }
    
    
}
