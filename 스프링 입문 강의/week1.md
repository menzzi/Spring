# 1주차

# 스프링 웹개발 기초

## 정적컨텐츠

서버에서 하는 일 없이 그대로 웹 브라우저에 파일을 보여주는 방식

→ 원하는 파일을 넣으면 그대로 반환, 프로그래밍을 할 수는 없음.

## MVC와 템플릿 엔진

서버에서 변형을 해서 html을 바꿔서 내려주는 방식 (↔ 정적 컨텐츠)

🔍 MVC(Model, View, Controller)

## API(**Application Programming Interface)**

JSON이라는 데이터 구조 포멧으로 클라이언트에게 데이터를 전달하는 방식 ex) 서버끼리 통신할 때

- @ResponseBody : Http의 Body에 문자 내용을 직접 return (소스코드 보면 html같은게 없고 문자가 그대로 내려감) - 객체가 JSON으로 변환 됨.
- JSON : 속성-값 쌍, 배열 자료형, 기타 모든 시리얼화 가능한 값 또는 "키-값 쌍"으로 이루어진 구조 ex) {”name” : ”spring!!!”} → {”key” : “value”}

---

# 회원관리 예제 - 백엔드 개발

## 비지니스 요구사항 정리

📌 데이터 저장소가 선정되지 않는 상황

<aside>
💡 우선 단순한 Memory 구현체로 만들고 기술이 선정되면 바꿔끼우기 위해 interface로 설계

</aside>

## 회원 도메인과 리포지토리 만들기

- Optional<T> : NPE(NullPointerException)를 방지할 수 있도록 도와준다 (Java8)
    - Null이 올 수 있는 값을 감싸주는 Wrapper 클래스

## 회원 리포지토리 **테스트 케이스** 작성

- 자바는 JUnit이라는 프레임워크로 테스트를 실행
    
    @Test 
    
    ```java
    import org.junit.jupiter.api.Test;
    ```
    

- 테스트들의 순서는 보장이 안 됨 → 순서에 의존해서 테스트를 짜면 안 됨
    
    🌟 테스트가  하나 끝나고 나면 데이터를 clear 해줘야 한다.
    
    ```java
    @AfterEach // 하나의 메서드가 실행이 끝날때마다 이 동작이 실행되게 함.
    public void afterEach() {
    	repository.clearStore();
    }
    ```
    

## 회원 서비스 개발

- 중복 회원 ❌ → ifPresent() 메서드  사용

```java
Optional<Member> result = memberRepository.findByName(member.getName());
result.ifPresent(m -> {
	throw new IllegalstateException("이미 존재하는 회원입니다.");
});

//Optional로 감쌌기 때문에 if(result == null) 과정 없이 
//ifPresent() 메서드 바로 사용 가능함.
```

```java
memberRepository.findByName(member.getName());
				.ifPresent(m -> {
						throw new IllegalstateException("이미 존재하는 회원입니다.");
				});
```

## 회원 서비스 테스트

🌟 테스트가  하나 끝나고 나면 데이터를 clear 해줘야 한다.

- command + shift + T → Create New Test 단축키 (테스트는 과감하게 한글로 해도 됨.)
- **given, when, then 문법** : 뭔가가 주어졌는데(given), 이것을 실행 했을 때(when), 결과가 이게 나와야 됨(then).
- 잘 실행되는지도 중요하지만 예외가 터트려지는지도 확인 해야함.
    - 방법 1 : try catch문
    - 방법 2 : assertThrows 메서드(반환값 있음)
- **DI**(Dependency Injection)
    - 다른 객체의 리포지토리를 생성해서 테스트 → 내용물이 달라질 수 있음
        
        📌 MemberService 에서 사용하는 MemoryMemberRepository랑 MemberServiceTest 에서 만든 MemoryMemberRepository가 서로 다른 리포지토리(인스턴스)인 상황.
        
        - 코드
            
            ```java
            public class MemberService{
            
            	private final MemberRepository memberRepository;
            
            	public MemberService(MemberRepository memberRepository){
            			this.memberRepository = memberRepository;
            	} //new 해서 생성x 외부에서 넣어주도록 바꿔줌.
            .
            .
            .
            ```
            
            ```java
            class MemberServiceTest{
            
            	MemberService memberService;
            	MemoryMemberRepository memberRepository;
            
            	@BeforeEach //테스트를 실행할때마다 생성
            	public void beforeEach(){
            		memberRepository = new MemoryMemberRepository();
            		memberService= new MemberService(memoryRepository);
            	}
            .
            .
            .
            ```
            

---

# 스프링 빈과 의존관계

## 컴포넌트 스캔과 자동 의존관계 설정

MemberController가 MemberService를 통해 회원가입을 하고 조회를 함(서로 의존관계)

- **@Autowired** : 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어준다(**DI**).
    - 스프링 빈에 등록된 객체에서만 동작한다.
- @Service 사용해서 MemberService를 스프링 빈에 등록
- @Repository 사용해서 MemoryMemberRepository를 스프링 빈에 등록

> @Component 를 포함하는 다음 애노테이션도 스프링 빈으로 자동 등록된다. → 컴포넌트 스캔
- @Controller
- @Service
- @Repository
> 

## 자바 코드로 직접 스프링 빈 등록하기

컴포넌트 스캔 없이 직접 스프링 빈에 등록

회원 서비스와 회원 리포지토리의 @Service, @Repository, @Autowired 애노테이션을 제거

```java
@Configuration
public class SpringConfig{
		
		@Bean
		public MemberService memberService() {
			return new MemberService(memberRepository());
		}

		@Bean
		public MemberRepository memberRepository() {
			return new MemoryMemberRepository();
			//구현 클래스를 변경하게 되면 설정파일만 바꾸면 됨.
			// MemoryMemberRepository()->DbMemberRepository()
		}
}
```

@Controller는 어차피 스프링이 관리하는 것이기 때문에 삭제하지 않고 컴포넌트 스캔

---

# 회원 관리 예제 - 웹 MVC 개발

## 회원 웹 기능 - 홈 화면 추가

- 홈 컨트롤러 만들기
    - @GetMapping(”/”) : [localhost:8080](http://localhost:8080) 으로 들어오면 바로 보이는 도메인 첫번째
        
        기본적으로 URL창에 엔터를 치는것은 getmapping, 조회할 때 주로 사용
        

## 회원 웹 기능 - 등록

- 회원 등록 컨트롤러
    - @PostMapping
        
        데이터를 form 같은 곳에 넣어서 전달할 때 쓰는 방식, 등록할 때 주로 사용
        

## 회원 웹 기능 - 조회

- 아직까지는 Memory안에 있기 때문에 자바를 내리면 데이터 다 사라짐.