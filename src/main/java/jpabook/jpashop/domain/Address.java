package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable //jpa의 내장타입이라는 것 (어딘가에 내장이 될 수 있다)
@Getter //값 타입은 변경 불가능하게 설계해야 한다. @Setter 를 제거하고, 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만들자.
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address () { //protected 설정이 더 안전, 얘를 함부로 new로 생성하면 안되겠네
    }

    public Address(String city, String street, String zipcode) { //얘는 public으로 열려있으니까 얘를 써야되겠네
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
