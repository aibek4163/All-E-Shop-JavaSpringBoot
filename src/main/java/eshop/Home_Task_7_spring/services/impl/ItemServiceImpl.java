package eshop.Home_Task_7_spring.services.impl;

import eshop.Home_Task_7_spring.entities.Brand;
import eshop.Home_Task_7_spring.entities.Category;
import eshop.Home_Task_7_spring.entities.Country;
import eshop.Home_Task_7_spring.entities.Item;
import eshop.Home_Task_7_spring.repositories.BrandRepository;
import eshop.Home_Task_7_spring.repositories.CategoryRepository;
import eshop.Home_Task_7_spring.repositories.CountryRepository;
import eshop.Home_Task_7_spring.repositories.ItemRepository;
import eshop.Home_Task_7_spring.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.getOne(id);
    }

    @Override
    public void deleteItem(Item item) {
        itemRepository.delete(item);
    }

    @Override
    public Item updateItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Item> findAllByInTopPageIsTrue() {
        return itemRepository.findAllByInTopPageIsTrue();
    }

    @Override
    public List<Item> findAllByNameContaining(String name) {
        return itemRepository.findAllByNameContaining(name);
    }

    @Override
    public List<Item> findAllByNameContainingAndPriceGreaterThanEqual(String name, double price_from) {
        return itemRepository.findAllByNameContainingAndPriceGreaterThanEqual(name,price_from);
    }

    @Override
    public List<Item> findAllByNameContainingAndPriceBetween(String name, double price_from, double price_to) {
        return itemRepository.findAllByNameContainingAndPriceBetween(name,price_from,price_to);
    }

    @Override
    public List<Item> findAllByNameContainingAndPriceLessThanEqual(String name, double price_to) {
        return itemRepository.findAllByNameContainingAndPriceLessThanEqual(name,price_to);
    }

    @Override
    public List<Item> findAllByBrandNameLikeAndNameLikeAndPriceBetweenOrderByPriceAsc(String brandName, String itemName, double price_to, double price_from) {
        return itemRepository.findAllByBrandNameLikeAndNameLikeAndPriceBetweenOrderByPriceAsc("%"+brandName+"%","%"+itemName+"%",price_to,price_from);
    }

    @Override
    public List<Item> findAllByBrandNameLikeAndNameLikeAndPriceBetweenOrderByPriceDesc(String brandName, String itemName, double price_to, double price_from) {
        return itemRepository.findAllByBrandNameLikeAndNameLikeAndPriceBetweenOrderByPriceAsc("%"+brandName+"%","%"+itemName+"%",price_to,price_from);
    }

    @Override
    public List<Item> findAllByPriceGreaterThanEqual(double price_from) {
        return itemRepository.findAllByPriceGreaterThanEqual(price_from);
    }

    @Override
    public List<Item> findAllByPriceLessThanEqual(double price_to) {
        return itemRepository.findAllByPriceLessThanEqual(price_to);
    }

    @Override
    public List<Item> findAllByPriceBetween(double price_from, double price_to) {
        return itemRepository.findAllByPriceBetween(price_from,price_to);
    }

    @Override
    public List<Item> findAllByNameOrderByPriceAsc(String name) {
        return itemRepository.findAllByNameOrderByPriceAsc(name);
    }

    @Override
    public List<Item> findAllByNameOrderByPriceDesc(String name) {
        return itemRepository.findAllByNameOrderByPriceDesc(name);
    }

    @Override
    public List<Item> findAllByInTopPageIsFalse() {
        return itemRepository.findAllByInTopPageIsFalse();
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand addBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand updateBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.getOne(id);
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Country addCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public Country updateCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public Country getCountryById(Long id) {
        return countryRepository.getOne(id);
    }

    @Override
    public void deleteBrand(Brand brand) {
        brandRepository.delete(brand);
    }

    @Override
    public void deleteCountry(Country country) {
        countryRepository.delete(country);
    }

    @Override
    public List<Brand> findBrandsByNameContaining(String name) {
        return brandRepository.findBrandsByNameContaining(name);
    }

    @Override
    public List<Item> findAllByBrand_Name(String name) {
        return itemRepository.findAllByBrand_Name(name);
    }

    @Override
    public List<Item> findAllByBrand_NameOrderByPriceAsc(String name) {
        return null;
    }

    @Override
    public List<Item> findAllByBrand_NameOrderByPriceDesc(String name) {
        return null;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }
}
