package net.class101.homework1.order.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
                .containsSequence(secondProduct, thirdProduct);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
