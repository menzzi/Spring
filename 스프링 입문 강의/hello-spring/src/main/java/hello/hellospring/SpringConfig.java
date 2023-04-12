package hello.hellospring;

import hello.hellospring.aop.TimeTraceAop;
import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

   private final MemberRepository memberRepository;

   @Autowired //생성자가 하나인 경우는 생략해도 됨
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

    //직접 등록해서 쓰는게 좋음 -> 아 이렇게 등록해서 쓰는구나 알 수 있음.
    //근데 여기선 그냥 컴포넌트 스캔 쓴대,,
//    @Bean
//    public TimeTraceAop timeTraceAop(){
//       return new TimeTraceAop();
//    }


//    @Bean
//    public MemberRepository memberRepository() {
//        return new JdbcMemberRepository(dataSource);
//        return new MemoryMemberRepository();
//        return new JdbcTemplateMemberRepository(dataSource);
//        return new JpaMemberRepository(em);
//    }
}
