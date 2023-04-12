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
