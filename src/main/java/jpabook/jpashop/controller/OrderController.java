package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model){

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId, //html에서 value가 name으로 넘어오게 됨
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {

        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch")OrderSearch orderSearch, Model model){ //orderSearch에 값이 있는 상태로 넘어감
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
//        model.addAttribute("orderSearch", orderSearch); --> @ModelAttribute("orderSearch")OrderSearch orderSearch

        //처음에는 아무것도 없이 넘어가다가(데이터가 없어서 안뿌림), orderList.html에서 memberName이랑 orderStatus 선택하고 form 던지면
        //값이 있는 상태로 orderSearch에 binding돼서 다시 여기로 돌아와서 (orderSearch에 내용이 담긴 채로 넘어옴)
        //이제 값이 있으므로 orderSearch가 html에서 다 뿌림(데이터가 바인딩 된 상태로 orderList로 넘어감)
        return "order/orderList";

    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

}
