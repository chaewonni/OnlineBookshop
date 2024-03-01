package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    //상품 저장
    public void save(Item item){
        if(item.getId() == null) { //아이템은 처음에 데이터 저장할 때 id가 없음 (완전히 새로 생성한 객체)
            em.persist(item);
        } else { //이미 db에 등록된 걸 어디서 가져옴. 여기서 save는 update와 비슷
            em.merge(item);
        }
    }

    //item 하나 조회
    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    //item 여러개 조회
    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
