package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //유닛 실행할 때 스프링이랑 같이 엮어서 실행할래
@SpringBootTest
@Transactional //트랜잭션 커밋을 안하고 롤백을 해버림(테스트 케이스에서 사용될 때만)
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
//    @Rollback(false) //롤백 안하고 커밋 해버림 (insert문 볼 수 있음)
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        em.flush(); //인서트 부분 볼 수 있고 실제 트랜잭션은 롤백을 하게 됨
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test//(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
//        memberService.join(member1);
//        try{
//            memberService.join(member2); //예외가 발생해야 한다!!
//        }catch (IllegalStateException e){
//            return;
//        }
        memberService.join(member1);
//        memberService.join(member2); //예외가 발생해야 한다!!

        //then
//        fail("예외가 발생해야 한다.");
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2); });
    }
}