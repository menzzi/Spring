package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        //ThreadA : A 사용자 10000원 주문
        int userAPrice =  statefulService1.order("userA", 10000);
        //ThreadB : B 사용자 20000원 주문
        int userBPrice = statefulService2.order("userB", 20000);

        //TreadA : 사용자A 주문 금액 조회 -> 사용자A가 주문하고 금액을 조회하는 도중에 B가 끼어들어서 주문을 한 상황
//        int price = statefulService1.getPrice();
        System.out.println("price = " + userAPrice);
        //20000 출력. Service1이든 2이든 같은 인스턴스 공유하기에..

//        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000); //망해따.

    }

    static class TestConfig{
        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }

}