package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //읽기에는 가급적이면 read-only 넣어줘야 데이터 변경 안됨
//중요. 데이터 변경하는 것은 트랜잭션이 꼭 있어야 함
@RequiredArgsConstructor //final이 있는 필드만 가지고 생성자를 만들어줌
public class MemberService {

    private final MemberRepository memberRepository; //변경할 일이 없기 때문에 final로

//    @Autowired //생성자가 한 개만 있는 경우에는 스프링이 자동으로 인젝션을 해줌
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    } --> @RequiredArgsConstructor로 인해 필요가 없어짐

    /**
     * 회원 가입(회원 등록)
     */
    @Transactional //쓰기에는 readOnly하면 안되니까 따로 Transactional넣어줌
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId(); //id라도 돌려줘야 뭐가 저장됐는지 알 수 있음
    }

    private void validateDuplicateMember(Member member){ //실무에서는 한 번 더 최후의 방어(멀티스레드 상황 고려) -> 멤버의 네임을 유니크 제약조건으로 잡는 것을 권장
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회(회원 목록 조회)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 단건 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id); //member가 영속상태
        member.setName(name); //영속 상태이므로 JPA가 플러쉬하고 영속성 컨텍스트 커밋 다 해버림
    }
}
