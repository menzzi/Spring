package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor // final붙어있는 것을 파라미터로 생성자를 만들어줌
public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository; // final-> 값이 있어야함.
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository; // 생략하면 오류
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        //설계가 잘됨(단일 체계 원칙 잘 지킴) ->OrderService 입장에서는 할인에 대해서 모름
        //                              discount 에서 할인은 알아서 하고 결과만 던져줘
        //나중에 할인 변경이 필요할 때 할인만 변경하면 됨.

        return new Order(memberId, itemName, itemPrice, discountPrice);

    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
