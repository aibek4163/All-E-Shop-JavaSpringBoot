package eshop.Home_Task_7_spring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;


    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "stars")
    private int stars;

    @Lob
    @Column(name = "small_picture_url",length = 1000000)
    private String small_picture_url;

    @Lob
    @Column(name = "large_picture_url",length = 1000000)
    private String large_picture_url;


    @Column(name = "added_date")
    private Timestamp added_date;

    @Column(name = "inTopPage",columnDefinition = "tinyint(1) default 0")
    private boolean inTopPage;

    @ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Category> categories;
}
