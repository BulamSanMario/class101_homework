package net.class101.homework1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import net.class101.homework1.order.controller.OrderController;

@Component
@Profile("!test")
public class Homework1Runner implements ApplicationRunner{

    @Autowired
    private OrderController orderController;
    
    public Homework1Runner(OrderController orderController) {
        this.orderController = orderController;
    }
    
    public void run(ApplicationArguments args) throws Exception {
        //주문 프로그램 실행
        orderController.initOrder();
    }
}
