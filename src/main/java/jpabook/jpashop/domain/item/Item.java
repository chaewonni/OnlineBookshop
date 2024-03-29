package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // single_table(한 테이블)에 다 때려넣음
@DiscriminatorColumn(name = "dtype") //single_table이기 때문에 상속 관계 맵핑
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==// !Setter를 가지고 하는 게 아니라 여기서 비즈니스 로직 작성! (가장 객체 지향적)
    //재고를 늘리고 줄이는 걸 할 것. 엔티티 자체가 해결할 수 있는 것들은 주로 엔티티 안에 비즈니스 로직을 넣는게 좋음
    //데이터(stackQuantity)를 가지고 있는 쪽에 비즈니스 메서드가 있는 게 가장 좋음(응집력 있음)
    /**
     * stock 증가
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
