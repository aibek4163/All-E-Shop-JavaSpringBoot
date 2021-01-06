package eshop.Home_Task_7_spring.services;

import eshop.Home_Task_7_spring.entities.Brand;
import eshop.Home_Task_7_spring.entities.Category;
import eshop.Home_Task_7_spring.entities.Country;
import eshop.Home_Task_7_spring.entities.Item;

import java.util.List;

public interface ItemService {
    Item addItem(Item item);
    List<Item> getAllItems();
    Item getItemById(Long id);
    void deleteItem(Item item);
    Item updateItem(Item item);
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

    List<Item> findAllByBrandNameLikeAndNameLikeAndPriceBetweenOrderByPriceAsc(String brandName,String itemName,double price_to,double price_from);
    List<Item> findAllByBrandNameLikeAndNameLikeAndPriceBetweenOrderByPriceDesc(String brandName,String itemName,double price_to,double price_from);

    List<Brand> getAllBrands();
    Brand addBrand(Brand brand);
    Brand updateBrand(Brand brand);
    Brand getBrandById(Long id);
    void deleteBrand(Brand brand);
    List<Brand> findBrandsByNameContaining(String brand_name);
    List<Item> findAllByBrand_Name(String name);
    List<Item> findAllByBrand_NameOrderByPriceAsc(String name);
    List<Item> findAllByBrand_NameOrderByPriceDesc(String name);

    List<Country> getAllCountries();
    Country addCountry(Country country);
    Country updateCountry(Country country);
    Country getCountryById(Long id);
    void deleteCountry(Country country);

    List<Category> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category);
    Category getCategoryById(Long id);
    void deleteCategory(Category category);
}
