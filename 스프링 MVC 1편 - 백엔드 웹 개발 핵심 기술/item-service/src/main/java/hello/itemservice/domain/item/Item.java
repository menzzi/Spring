package hello.itemservice.domain.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data //-> 위험
//@Getter @Setter -> 권장
public class Item {

    private Long id;
    private String itemName;
    private Integer price; //price 가 안들어갈때도 있다고 가정 -> Integer
    private Integer quantity; // 만약 int 면 0이라도 들어가야함

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
