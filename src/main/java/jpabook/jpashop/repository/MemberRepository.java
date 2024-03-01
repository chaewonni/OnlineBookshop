package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //스프링 빈으로 등록
@RequiredArgsConstructor //2
public class MemberRepository {

//    @PersistenceContext //jpa가 제공하는 표준 annotation --> 1로 인해 사라지고
//    @Autowired --> 1로 인해 생겨나고 2로 인해 사라지고
    private final EntityManager em; //spring이 entitymanager만들어서 주입해줌
    //2로 인해 final로 수정

//    1
//    public MemberRepository(EntityManager em){
//        this.em = em;
//    }

    //회원 저장
    public void save(Member member){
        em.persist(member);
    }

    //회원 조회 (단건 조회)
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    //리스트 조회
    public List<Member> findAll() {
//        List<Member> result = em.createQuery("select m from Member m", Member.class)
//                .getResultList();
//
//        return result;
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //회원을 이름에 의해서 조회
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
