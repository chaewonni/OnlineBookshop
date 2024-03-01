package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Array;
import java.time.LocalDateTime; //hibernate가 알아서 지원
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //다대일 관계
    @JoinColumn(name = "member_id") //FK이름이 member_id가 됨
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    //cascade --> order를 persist하면 여기 안에 들어와있는 orderItem도 다 강제로 persist날려줌
    private List<OrderItem> orderItems = new ArrayList<>();

//    persist(orderItemA)
//    persist(orderItemB)
//    persist(orderItemC)
//    persist(order) --> Cascade하면 이것만 있으면 됨 cascade가 persist 전파, delete할 때도 같이 지워버림

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //FK키를 delivery에 둬도 되고 order에 둬도 되지만, 좀 더 접근이 많은 order에
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //테이블 명이 order_date로 바뀜
    private LocalDateTime orderDate; //주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문 상태 [ORDER, CANCEL]

    //==연관관계 편의 메서드==//
    //양방향일 때 쓰면 좋음
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this); //this는 order객체
    }
//    public static void main(String[] args){
//        Member member = new Member();
//        Order order = new Order();
//
//        member.getOrders().add(order);
//        order.setMember(member);
//    }
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==// 생성하는 지점 변경해야 되면 이것만 바꾸면 됨
    //각각 set set set 하는게 아니라 주문 생성에 대한 복잡한 비즈니스 로직을 다 여기에 응집해놓음
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.CAMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    //==조회 로직=//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
