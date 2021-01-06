package eshop.Home_Task_7_spring.db;

import java.util.ArrayList;

public class DBManager {

    private static ArrayList<Old_Item> oldItems = new ArrayList<>();

    static {
        oldItems.add(new Old_Item(1L,"Samsung S9","Samsung S9 2018",250000,20,5,"https://avatars.mds.yandex.net/get-mpic/199079/img_id7586904575745819075.jpeg/9hq"));
        oldItems.add(new Old_Item(2L,"Iphone 11","Iphone 11",400000,20,4,"https://avatars.mds.yandex.net/get-mpic/199079/img_id7586904575745819075.jpeg/9hq"));
        oldItems.add(new Old_Item(3L,"Huawei","Huawei",150000,20,3,"https://avatars.mds.yandex.net/get-mpic/199079/img_id7586904575745819075.jpeg/9hq"));
        oldItems.add(new Old_Item(4L,"Oppo","Oppo",120000,20,2,"https://avatars.mds.yandex.net/get-mpic/199079/img_id7586904575745819075.jpeg/9hq"));
        oldItems.add(new Old_Item(5L,"Nokia","Nokia",90000,20,1,"https://avatars.mds.yandex.net/get-mpic/199079/img_id7586904575745819075.jpeg/9hq"));
    }

    private static Long id = 5L;

    public static void addItem(Old_Item oldItem){
        oldItem.setId(id);
        oldItems.add(oldItem);
        id++;
    }

    public static ArrayList<Old_Item> getAllItems(){
        return oldItems;
    }

    public static Old_Item getItemById(Long id){
        for(Old_Item it: oldItems){
            if(it.getId().equals(id)){
                return it;
            }
        }
        return null;
    }

    public static void deleteItem(Long id){
        for(Old_Item it: oldItems){
            if(it.getId().equals(id)){
                oldItems.remove(it);
                break;
            }
        }
    }
}
