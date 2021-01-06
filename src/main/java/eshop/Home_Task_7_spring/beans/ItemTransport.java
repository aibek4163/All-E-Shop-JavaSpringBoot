package eshop.Home_Task_7_spring.beans;

import eshop.Home_Task_7_spring.entities.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemTransport {
    private List<Item> items = null;

    public ItemTransport() {
        items = new ArrayList<>();
    }

    public ItemTransport(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
