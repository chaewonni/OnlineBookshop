package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded //내장 타입을 포함했다
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member") //일대다 관계
    //order 테이블에 있는 member 필드에 의해서 나는 맵핑 된거야
    //나는 내가 맵핑을 하는 애가 아니고 그냥 맵핑된 거울일 뿐이야 //여기에 뭔가 값을 넣는다고 해서 FK값이 변경되지 않음
    private List<Order> orders = new ArrayList<>();
    //이 컬렉션을 가급적이면 변경하면 안됨(절대 바꾸지 마) cf)2장 14쪽
}
