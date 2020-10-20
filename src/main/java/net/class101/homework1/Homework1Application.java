package net.class101.homework1;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.class101.homework1.order.controller.OrderController;

@SpringBootApplication
public class Homework1Application implements ApplicationRunner {
    
    private OrderController orderController;
    
    public Homework1Application(OrderController orderController) {
        this.orderController = orderController;
    }

	public static void main(String[] args) {
	    SpringApplication app = new SpringApplication(Homework1Application.class);
        app.run(args);
	}
	
	@Override
    public void run(ApplicationArguments args) throws Exception {
        //주문 프로그램 실행
	    orderController.initOrder();
    }

}
