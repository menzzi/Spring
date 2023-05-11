package hello.core.member;

import org.springframework.stereotype.Component;

//관례상 구현체가 하나만 있을 때 인터페이스 명 뒤에 Impl 이라고 많이 쓴다.
@Component
public class MemberServiceImpl implements MemberService{


    //    private final MemberRepository memberRepository = new MemoryMemberRepository();
    //인터페이스만 가지고 있으면 NullPointerExeption 터질 수 있으니 구현체를 만들어 줘야  .(구현체 없이 Null이면 오류 터짐)
    //MemberServiceImpl은 추상화와 구체화에 의존.
    private final MemberRepository memberRepository;
    //이제 DIP 지킴
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
