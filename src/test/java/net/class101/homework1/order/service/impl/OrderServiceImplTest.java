package net.class101.homework1.order.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import net.class101.homework1.common.util.Const;
import net.class101.homework1.common.util.ExHashMap;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceImplTest {

    @Autowired
    OrderServiceImpl orderService;
    
    @SuppressWarnings("unchecked")
    @Test
    public void testSelectProductList() {
        try {
            List<String> list = (List<String>) orderService.selectProductList().get("list");
            
            String firstProduct = "16374    스마트스토어로 월 100만원 만들기, 평범한 사람이 돈을 만드는 비법    151950    99999";
            String secondProduct = "26825    해금, 특별하고 아름다운 나만의 반려악기    114500    99999";
            String thirdProduct = "65625    일상에 따뜻한 숨결을 불어넣어요, 반지수와 함께하는 아이패드 드로잉    174500    99999";
            
            assertThat(list)
                .isNotEmpty()
                .contains(firstProduct)
                .doesNotContainNull()
                .containsSequence(secondProduct, thirdProduct)
                .hasSize(20);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSelectProductInfo() {
        
        try {
            ExHashMap paramMap = new ExHashMap();
            paramMap.put("num", 39712);
            ExHashMap resultMap = orderService.selectProductInfo(paramMap);
            
            assertThat(resultMap)
                .isNotEmpty()
                .contains(
                        entry("gbn", Const.PRODUCT_TYPE.KIT.name()),
                        entry("num", 39712),
                        entry("qty", 8),
                        entry("title", "집에서 느끼는 겨울의 묵직한 포근함, 플랜테리어 아이템"),
                        entry("resultCode", Const.ERROR_CODE.SUCCESS));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
