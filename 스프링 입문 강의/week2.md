# 2주차

# 스프링 DB 접근 기술

## H2 데이터베이스 설치

## 순수 JDBC

정신 건강을 위해 참고만 하고 넘어가자😅

- 자바는 기본적으로 DB랑 붙을려면 JDBC가 꼭 필요하다.
- 개방-폐쇄 원칙(OCP : Open-Closed-Principle) → 기능을 변경을 해도 조립하는 코드만 수정하면 실제 동작 코드는 수정 안 해도 작동한다. (개방-폐쇄 원칙이 지켜진 상황)

## 스프링 통합 테스트

- @SpringBootTest : 스프링 컨테이너와 테스트를 함께 실행한다.
- **@Transactional** : 테스트 케이스에 이 애노테이션이 있으면, 테스트 시작 전에 트랜잭션을 시작하고, 테스트 완료 후에 항상 롤백한다. 이렇게 하면 DB에 데이터가 남지 않으므로 다음 테스트에 영향을 주지 않는다. (테스트 각각 하나하나 마다 적용, 테스트 케이스에 붙었을 때만 롤백)

이런 통합 테스트보다 단위 테스트(스프링 컨테이너 없이 테스트 가능한 테스트)를 잘 만드는 것이 더 중요하다.

## 스프링 JdbcTemplate

💡생성자가 딱 하나만 있으면 스프링 빈으로 등록됨 → @Autowired 생략 가능

## JPA

〰️ spring-boot-starter-data-jpa 추가 : 내부에 jdbc 관련 라이브러리를 포함한다. 따라서 jdbc는 제거해도 된다. (🐘 buld.gradle 에서!!)

- SQL과 데이터 중심의 설계에서 객체 중심의 설계로 패러다임을 전환할 수 있다.
- SQL은 결국 개발자가 직접 작성해야함 → JPA 사용하면 자동으로 처리해준다.

```java
spring.jpa.hibernate.ddl-auto=none
//spring.jpa.hibernate.ddl-auto=create none의 반대
```

- ORM기술 : JPAS는 ORM기술이다 (🔍 Object,Relational,Mapping = 객체와 데이터베이스의 관계를 매핑해주는 도구)
- @Entity : 이제부터 이거는 JPA가 관리하는 엔티티
- @GeneratedValue(strategy = GenerationType.*IDENTITY*)
    - IDENTITY 전략 : DB에 값을 넣으면 DB가 자동으로 ID를 생성해주는 전략

<JPA 사용한 코드> domain 패키지>member 클래스

```java
package hello.hellospring.domain;

import jakarta.persistence.*;

@Entity // 이제부터 이거는 JPA가 관리하는 엔티티.
public class Member  {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //@Column(name="username") 
		//만약에 DB의 Colume이 username이면 애노테이션으로 매핑
    //이렇게 애노테이션을 가지로 DB랑 매핑
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

- EntityManager : JPA는  EntityManager로 동작을 함
    
    → bulid.grable에서 data-jpa를 라이브러리로 받아서 스프링부트가 자동으로 EntityManager 생성해줌.(그래서 우리는 생성된걸 injection해주면 됨)
    
- persist() : 영속하다, 영구 저장하다. → JPA가 insert Query 만들어서 DB에 다 집어넣고 ID까지 멤버에다가 setId해줌.
- find() : find(조회할 타입,식별자 PK값)
- createQuery() : `createQuery("select m from Member m", Member.class)` Member(Entity)를 대상으로 Query 날림, select m → select *이 아님 멤버(Entity)자체를 select 하는것

저장하고 조회하고 업데이트하는건 SQL로 짤 필요 ❌(단건을 찾는것)

여러개의 List를 가지고 돌릴때는(↔ 단건)  JPQL을 작성해 줘야함 EX)`createQuery("select m from Member m", Member.class)` 

⚡️JPA 기술을 스프링에 감싸서 제공하는 기술 (스프링 데이터 JPA) 를 사용하면 JPQL 안 짜도 됨

repository 패키지> JpaMemberRepository 클래스

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{

    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member); //JPA가 insert Query 만들어서 DB에 다 집어넣고 ID까지 멤버에다가 setId해줌.
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id); //조회 -> find(조회할 타입,식별자 PK)
        return Optional.ofNullable(member);
    }

    //위와 달리 여러개의 List를 가지고 돌릴때는(↔ 단건) JPQL을 작성해 줘야함
    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                //select m → 멤버(Entity)자체를 select 하는것
                //Member(Entity)를 대상으로 Query 날림
                .getResultList();
    }
}
```

🔅Query : 데이터 베이스에 정보를 요청

🔅PK : 기본키

## 스프링 데이터 JPA

구현 클래스를 작성할 필요 없이 인터페이스로 끝난다.

🔆 interface가 interface를 받을 땐 implements가 아닌 extends

🔆 interface는 다중 상속 가능

repository 패키지> SpringDataJpaMemberRepository 

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member,Long> ,MemberRepository {
    //id 타입 Long
    //스프링 데이터 JPA가 자동으로 구현체를 등록해준다. 
		//스프링 빈에 올려줘서 우리는 injection해서 사용 
    @Override
    Optional<Member> findByName(String name);
		//JPQL select m from Member m where m.name = ?
		//findByName(), findByEmail()처럼 메서드 이름 만으로 조회 기능 제공
}
```
<img width="479" alt="스크린샷 2023-04-07 오후 7 45 10" src="https://user-images.githubusercontent.com/124131845/230834973-5b148c0c-94b9-4688-a726-03c8fb4b4655.png">

## AOP

## AOP가 필요한 상황

~~c언어의 포인터 같은 존재…~~

📌 모든 메서드의 호출 시간을 측정하고 싶음

<aside>
👉🏻 호출 시간을 측정하는 기능은 핵심 비지니스 로직이 아니다.(핵심 관심 사항 core concern)
시간을 측정하는 로직은 공통의 로직이다.(공통 관심 사항 cross-cutting concern)
시간을 측정하는 로직과 핵심 비즈니스의 로직이 섞여서 유지보수가 어렵다.
시간을 측정하는 로직을 별도의 공통 로직으로 만들기 매우 어렵다.
시간을 측정하는 로직을 변경할 때 모든 로직을 찾아가면서 변경해야 한다.

</aside>

→ 곤란한 힘든 상황‼️ ➡️ AOP가 해결

## AOP 적용

🔍 Aspect Oriented Programming : 관점 지향 프로그래밍

<img width="631" alt="스크린샷 2023-04-07 오후 8 26 45" src="https://user-images.githubusercontent.com/124131845/230835187-a2ad7c14-e3e8-4061-96bf-f27a8adbfc4b.png">

<img width="627" alt="스크린샷 2023-04-07 오후 8 27 32" src="https://user-images.githubusercontent.com/124131845/230835242-0e4ca9ee-8bd8-45b2-a361-7b515c0a6a79.png">


- @Aspect 적어줘야 AOP로 쓸 수 있다.
- 컴포넌트 스캔을 쓰기도 하는데 스프링 빈에 등록하는 것을 더 선호(정형화❌ 좀 특별하니까)

```java
//직접 등록해서 쓰는게 좋음 -> 아 이렇게 등록해서 쓰는구나 알 수 있음.
    @Bean
    public TimeTraceAop timeTraceAop(){
       return new TimeTraceAop();
    }
```

TimeTraceAop 클래스

```java
package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component //빈으로 직접 등록이 좋으나 여기선 컴포넌트 스캔함.
public class TimeTraceAop {

    @Around("execution(* hello.hellospring..*(..))")
    public Object execute (ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        System.out.println("START: "+joinPoint.toString());
        try{
            //다음 메서드로 진행
            return joinPoint.proceed();
        }finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: "+joinPoint.toString()+" "+ timeMs + "ms");

        }
    }
}
```

<aside>
🔥 해결
회원가입, 회원 조회등 핵심 관심사항과 시간을 측정하는 공통 관심 사항을 분리한다.
시간을 측정하는 로직을 별도의 공통 로직으로 만들었다.
핵심 관심 사항을 깔끔하게 유지할 수 있다.
변경이 필요하면 이 로직만 변경하면 된다.
원하는 적용 대상을 선택할 수 있다. → "execution(* hello.hellospring..*(..))" 보통 이렇게 패키지 레벨로 하지만 변경 가능  ➡️"execution(* hello.hellospring.service..*(..))"

</aside>

- AOP 동작방식

 <img width="500" alt="스크린샷 2023-04-10 오후 1 02 38" src="https://user-images.githubusercontent.com/124131845/230835339-d0c29ff4-0532-4db2-8729-77b27b4f259b.png">

    
 <img width="600" alt="스크린샷 2023-04-10 오후 1 03 17" src="https://user-images.githubusercontent.com/124131845/230835444-fe37eeab-a1da-4ca9-b218-528bc61270e8.png">
 스프링은 AOP가 있으면 가짜 멤버 서비스(프록시)를 만들어 냄. 이 프록시를 앞세워두고 joinPoint.proceed()같은걸 했을 때 실제 멤버 서비스를 호출해준다.
 
 
 
    
   <img width="600" alt="스크린샷 2023-04-10 오후 1 24 08" src="https://user-images.githubusercontent.com/124131845/230835549-99e33635-c06a-4da1-adfb-7053ce4f482a.png">
 가짜를 만들어서 DI해줌
