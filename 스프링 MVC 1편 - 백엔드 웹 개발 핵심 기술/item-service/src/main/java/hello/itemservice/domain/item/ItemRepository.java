package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository //-> 컴포넌트 스캔의 대상이 됨.(@Component)
public class ItemRepository {

    private static final Map<Long,Item> store = new HashMap<>(); //static 사용, 실제로는 HashMap 사용하면 안됨.
    private static long sequence = 0L; // static

    //스프링컨테이너 안에서 쓰면 어차피 싱글톤이여서 static 사용 안 해도 되긴한다. 따로 new 해서 사용하는 경우 static
    //안 해두면 객체 생성한 만큼 store 생성됨..
    //실무에서는 ConcurrentHashMap 과 어터믹롱?다른 롱을 사용해야함

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(),item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values());
        //안전하게 감싸서 반. 타입을 안 바꿔도 되고 ArrayList에 값을 넣어도 store에 변화 없음.
    }

    public void update(Long itemId,Item updateParam){
        //먼저 업데이트를 할려면 아이템을 찾아야함..
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore(){
        store.clear();
    }
}
