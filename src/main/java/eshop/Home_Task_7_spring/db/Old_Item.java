package eshop.Home_Task_7_spring.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Old_Item {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int amount;
    private int stars;
    private String small_picture_url;
}
