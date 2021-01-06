package eshop.Home_Task_7_spring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "purchase_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "added_date")
    private Timestamp buyed_date;

    @Column(name = "sold_amount")
    private int amount;

    @OneToOne(fetch = FetchType.EAGER)
    private Item item;
}
