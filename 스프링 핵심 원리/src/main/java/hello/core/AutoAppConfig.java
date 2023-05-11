package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
                classes = Configuration.class)
        //AppConfig 클래스는 수동으로 직접 등록하는 코드임으로 자동으로 등록 되면 안 됨.(충돌)
        //@Configuration 애노테이션이 붙어있는데 이게 @Component가 붙어있어서 자동 스캔 대상임.
        //실무에선 잘 안 함. 예제 코드 지우지 않고 진행하기 위해 한 것.
)
public class AutoAppConfig {

//    @Bean(name = "memoryMemberRepository")
//    MemberRepository memoryRepository() {
//        return new MemoryMemberRepository();
//    }
}
