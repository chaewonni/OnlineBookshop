package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional //이거에 의해 트랜잭션 커밋되고 JPA는 플러시라는 거 날려서 엔티티 중에 변경된 애 다 찾아서 바뀐 값으로 업데이트 코드를 db에 날림
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        Item findItem = itemRepository.findOne(itemId); //아이디를 기반으로 실제 db에 있는 영속성 엔티티를 찾아옴
//        findItem.change(name, price, stockQuantity); //이렇게 하는 게 더 좋음 (추적하기에 더 좋음)
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
//        itemRepository.save(findItem); 이거 호출해줄 필요X -> 이외의 것들 아무것도 호출할 필요 없음
//        return findItem;
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}