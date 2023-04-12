package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member,Long> ,MemberRepository {
    //id 타입 Long
    //스프링 데이터 JPA가 자동으로 구현체를 등록해준다. 스프링 빈에 올려줘서 우리는 injection해서 사용
    @Override
    Optional<Member> findByName(String name);
}
