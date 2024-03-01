package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new") //get은 form화면을 열어보고
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        //멤버 폼이라는 걸 넘겼기 때문에 화면에서 객체에 접근할 수 있게 됨
        return "members/createMemberForm";
    }

    @PostMapping("/members/new") //post는 이 데이터를 실제 등록
    public String create(@Valid MemberForm form, BindingResult result) {
        //Valid: 회원 이름 필수로

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }


}
