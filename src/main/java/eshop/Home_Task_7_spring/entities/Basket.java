package eshop.Home_Task_7_spring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Basket {
    private static final AtomicInteger c = new AtomicInteger(0);
    private int id;
    private Item items;
    private int amount;
    private int total = 0;

    public Basket(Item items, int amount) {
        this.id = c.incrementAndGet();
        this.items = items;
        this.amount = amount;
    }

    public int calculate(List<Basket> baskets){
        int sum = 0;
        for(int i = 0;i<baskets.size();i++) {
            sum += baskets.get(i).getAmount() * baskets.get(i).getItems().getPrice();
        }
        return sum;
    }
}
