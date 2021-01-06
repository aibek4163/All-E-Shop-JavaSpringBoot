package eshop.Home_Task_7_spring.repositories;

import eshop.Home_Task_7_spring.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Item,Long> {
    List<Item> findAllByInTopPageIsTrue();
    List<Item> findAllByNameContaining(String name);
    List<Item> findAllByNameContainingAndPriceGreaterThanEqual(String name,double price_from);
    List<Item> findAllByNameContainingAndPriceBetween(String name,double price_from,double price_to);
    List<Item> findAllByNameContainingAndPriceLessThanEqual(String name,double price_to);
    List<Item> findAllByPriceGreaterThanEqual(double price_from);
    List<Item> findAllByPriceLessThanEqual(double price_to);
    List<Item> findAllByPriceBetween(double price_from,double price_to);
    List<Item> findAllByNameOrderByPriceAsc(String name);
    List<Item> findAllByNameOrderByPriceDesc(String name);
    List<Item> findAllByInTopPageIsFalse();
    List<Item> findAllByBrand_Name(String name);
    List<Item> findAllByBrandNameLikeAndNameLikeAndPriceBetweenOrderByPriceAsc(String brandName,String itemName,double price_to,double price_from);
    List<Item> findAllByBrandNameLikeAndNameLikeAndPriceBetweenOrderByPriceDesc(String brandName,String itemName,double price_to,double price_from);
    List<Item> findAllByBrand_NameOrderByPriceAsc(String name);
    List<Item> findAllByBrand_NameOrderByPriceDesc(String name);
}
