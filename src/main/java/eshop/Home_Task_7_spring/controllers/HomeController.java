package eshop.Home_Task_7_spring.controllers;

import eshop.Home_Task_7_spring.beans.ItemTransport;
import eshop.Home_Task_7_spring.entities.*;
import eshop.Home_Task_7_spring.services.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class HomeController {
//    @Autowired
//    private TestBean testBean;
//
//    @Autowired
//    private FirstBean firstBean;
//
//    @Autowired
//    private FirstBean secondBean;
//
//    @Autowired
//    private ThirdBean thirdBean;
//    @Autowired
//    private TestServices testServices;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemTransport itemTransport;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;

    @Value("${file.picture.viewPath}")
    private String viewPathItem;

    @Value("${file.picture.uploadPath}")
    private String uploadPathItem;

    @Value("${file.picture.defaultPicture}")
    private String defaultPictureItem;

    @Autowired
    private HttpSession session;

    @Autowired
    private CommentService commentService;

    @GetMapping(value = "/")
    public String index(Model model){

        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        List<Item> items = itemService.findAllByInTopPageIsTrue();
        items.addAll(itemService.findAllByInTopPageIsFalse());
        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("brands",brands);
        model.addAttribute("items", items);
//        testBean.setText("Aibek");
//        String test = firstBean.getText()+" "+secondBean.getText();
        //thirdBean.setData("JAVA");
        //String test = thirdBean.getData();
//        testServices.setTestData("adqwdwqd");
//        testServices.setTestIntData(16);
//        String test = testServices.getTestData() + " " +testServices.getTestDataInt();
//        model.addAttribute("text",test);
        return "index";
    }


    @GetMapping(value = "/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String add(Model model){
        model.addAttribute("currentUser",getUserData());
        model.addAttribute("basketSize",getBasketSize(session));
        List<Item> items = itemService.getAllItems();
        List<Country> countries = itemService.getAllCountries();
        List<Brand> brands = itemService.getAllBrands();
        List<Category> categories = itemService.getAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("items",items);
        model.addAttribute("countries",countries);
        model.addAttribute("brands",brands);

        return "add_item";
    }

    @PostMapping(value = "/add_item")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String add_item(@RequestParam (name = "item_name") String name,
                           @RequestParam(name = "item_description") String item_description,
                           @RequestParam(name = "item_price") double price,
                           @RequestParam(name = "item_rating") int item_rating,
                           @RequestParam(name = "item_small_picture") String sml_picture,
                           @RequestParam(name = "item_large_picture") String lrg_picture,
                           @RequestParam(name = "in_top") boolean inTopPage,
                           @RequestParam(name = "brand") Long brand_id,
                           @RequestParam(name = "category") Long category_id,
                           @RequestParam(name = "add_item",defaultValue = "no") String add_btn){
        Brand brand = itemService.getBrandById(brand_id);
        Category category = itemService.getCategoryById(category_id);

        if(add_btn.equals("add") && brand!=null && category!=null) {
            List<Category> categories = new ArrayList<>();
            categories.add(category);
            itemService.addItem(new Item(null, name, item_description, price, item_rating, sml_picture, lrg_picture, Timestamp.valueOf(LocalDateTime.now()), inTopPage, brand,categories));
        }
        return "redirect:/admin";
    }

    @GetMapping(value = "/details/{id}")
    public String details(Model model, @PathVariable(name = "id") Long id){
        model.addAttribute("currentUser",getUserData());
        Item item = itemService.getItemById(id);
        List<Picture> pictures = pictureService.findAllByItem_Id(id);
        List<Category> categories = item.getCategories();
        List<Comment> comments = commentService.findAllByItem_Id(id);

        model.addAttribute("comments",comments);
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("item",item);
        model.addAttribute("categories",categories);
        model.addAttribute("pictures",pictures);
        return "details";
    }

    @GetMapping(value = "/edit_details/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String edit_details(Model model, @PathVariable(name = "id") Long id){
        model.addAttribute("currentUser",getUserData());
        Item item = itemService.getItemById(id);
        List<Brand> brands = itemService.getAllBrands();
        List<Category> categories = itemService.getAllCategories();
        List<Picture> pictures = pictureService.findAllByItem_Id(id);
        model.addAttribute("pictures",pictures);
        model.addAttribute("categories",categories);
        model.addAttribute("brands",brands);
        model.addAttribute("item",item);
        model.addAttribute("basketSize",getBasketSize(session));
        return "details_items";
    }

    @GetMapping(value = "/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String admin_page(Model model){
        model.addAttribute("currentUser",getUserData());
        List<Item> items = itemService.getAllItems();
        List<Country> countries = itemService.getAllCountries();
        List<Brand> brands = itemService.getAllBrands();
        List<Category> categories = itemService.getAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("items",items);
        model.addAttribute("countries",countries);
        model.addAttribute("brands",brands);
        model.addAttribute("basketSize",getBasketSize(session));
        return "admin_page";
    }

    @PostMapping(value = "/edit_item")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String edit_item(@RequestParam(name = "item_id") Long id,
                            @RequestParam(name = "item_name") String name,
                            @RequestParam(name = "item_description") String item_description,
                            @RequestParam(name = "item_price") double item_price,
                            @RequestParam(name = "item_rating") int rating,
                            @RequestParam(name = "item_sml_picture") String item_sml_picture,
                            @RequestParam(name = "item_lrg_picture") String item_lrg_picture,
                            @RequestParam(name = "in_top") boolean in_top,
                            @RequestParam(name = "brand_id") Long brand_id,
                            @RequestParam(name = "btn",defaultValue = "null") String btn){
        if(btn.equals("update")){
            Item item = itemService.getItemById(id);
            Brand brand = itemService.getBrandById(brand_id);

            if(item!=null && brand!=null){
                item.setName(name);
                item.setDescription(item_description);
                item.setPrice(item_price);
                item.setStars(rating);
                item.setSmall_picture_url(item_sml_picture);
                item.setLarge_picture_url(item_lrg_picture);
                item.setInTopPage(in_top);
                item.setBrand(brand);
                itemService.updateItem(item);
            }
        }
        else if(btn.equals("delete")){
            Item  item = itemService.getItemById(id);
            if(item!=null) {
                itemService.deleteItem(item);
            }
        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete_item")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String delete_item(@RequestParam(name = "item_id") Long id,
                              @RequestParam(name = "delete_btn",defaultValue = "null") String del_btn){
        if(del_btn.equals("delete")){
            Item  item = itemService.getItemById(id);
            if(item!=null) {
                itemService.deleteItem(item);
            }
        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/set_category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit_category(@RequestParam(name = "item_id") Long item_id,
                                @RequestParam(name = "category_id") Long category_id,
                                @RequestParam(name = "btn") String set,Model model){
        model.addAttribute("currentUser",getUserData());
        Category category = itemService.getCategoryById(category_id);
        if(category!=null){
            Item item = itemService.getItemById(item_id);
            if(item!=null){
                List<Category> categories = item.getCategories();
                if(set.equals("set")){
                    if(categories==null){
                        categories = new ArrayList<>();
                    }
                    categories.add(category);
                    itemService.updateItem(item);
                }
                if(set.equals("unset")){
                    categories.remove(category);
                    itemService.updateItem(item);
                }
            }
        }
        return "redirect:/edit_details/"+item_id+"#categoriesDiv";
    }

    @GetMapping(value = "/unset_category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String unset_category(@RequestParam(name = "item_id") Long item_id,
                                @RequestParam(name = "category_id") Long category_id,Model model){
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        Category category = itemService.getCategoryById(category_id);
        if(category!=null){
            Item item = itemService.getItemById(item_id);
            if(item!=null){
                List<Category> categories = item.getCategories();
                categories.remove(category);
                itemService.updateItem(item);
            }
        }
        return "redirect:/edit_details/"+item_id;
    }

    @GetMapping(value = "/search")
    public String search(Model model,@RequestParam(name = "name") String name){
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        List<Item> founded_items = itemService.findAllByNameContaining(name);
        List<Brand> brands = itemService.getAllBrands();
        itemTransport.setItems(founded_items);
        for(Item i:founded_items){
            System.out.println("1st search");
            System.out.println(i.getName());
        }
        model.addAttribute("name",name);
        model.addAttribute("brands",brands);
        model.addAttribute("founded_items",founded_items);
        return "search_page";
    }

    @GetMapping(value = "/detailed_search")
    public String detailed_search(Model model,@RequestParam(name = "name",defaultValue = "null") String name,
                                  @RequestParam(name = "price_from",defaultValue = "0") double price_from,
                                  @RequestParam(name = "price_to",defaultValue = "10000000") double price_to,
                                  @RequestParam(name = "btn",defaultValue = "not") String btn_search,
                                  @RequestParam(name = "options",defaultValue = "null") String option,
                                  @RequestParam(name = "search_brand",defaultValue = "null") String search_brand,
                                  @RequestParam(name = "brand",defaultValue = "null") String brand){
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
//        List<Item> result = new ArrayList<>();
//        if(option.equals("asc")){
//            result =itemService.findAllByBrandNameLikeAndNameLikeAndPriceBetweenOrderByPriceAsc(brand,name,price_to,price_from);
//        }
//        else {
//            result = itemService.findAllByBrandNameLikeAndNameLikeAndPriceBetweenOrderByPriceDesc(brand,name,price_to,price_from);
//        }
//        model.addAttribute("founded_items",result);
        List<Item> founded_items = itemTransport.getItems();
        List<Item> brands =  itemService.findAllByBrand_Name(search_brand);
        List<Item> brands_2 = itemService.findAllByBrand_Name(brand);

        boolean contain = false;


        List<Item> result = new ArrayList<>();
        List<Item> list = new ArrayList<>();
        for(Item i:founded_items){
            System.out.println("2nd search");
            System.out.println(i.getName());
        }
        if(!search_brand.equals("null") ){
            list.addAll(brands);
            result.addAll(brands);
        }
        if(!brand.equals("null") && !founded_items.containsAll(brands_2)){
            for(int i = 0;i<brands_2.size();i++){
                if (founded_items.contains(brands_2.get(i))) {
                    System.out.println("sdfg");
                    founded_items = brands_2;
                    break;
                }
            }
            if(!founded_items.containsAll(brands_2)) {
                System.out.println("brand from searc page");
                founded_items.addAll(brands_2);
            }
        }

        if(btn_search.equals("btn_search")) {
            System.out.println("btn");
            System.out.println(name);
            List<Item> new_item = itemService.findAllByNameContaining(name);
            if(!name.equals("null") && !founded_items.containsAll(new_item)){
                founded_items = new_item;
            }
            for(Item i:founded_items){
                System.out.println(i.getName());
                if(!name.equals("null") && price_from==0 && price_to==0 && brand.equals("null")) {
                    if (i.getName().toLowerCase().contains(name.toLowerCase())){
                        result.add(i);
                    }
                }
//                else if(!name.equals("null") && !i.getName().contains(name)){
//                    System.out.println("fwefwef");
//                    List<Item> new_item = itemService.findAllByNameContaining(name);
//                    founded_items = new_item;
//                    result.addAll(founded_items);
//                    break;
//                }
                else if(!name.equals("null") && price_from!=0 && price_to==0 &&  brand.equals("null")){
                    if (i.getName().toLowerCase().contains(name.toLowerCase()) && price_from<=i.getPrice()){
                        result.add(i);
                    }
                }
                else if(!name.equals("null") && price_from!=0 && price_to!=0 && brand.equals("null")){
                    if (i.getName().toLowerCase().contains(name.toLowerCase()) && price_from<=i.getPrice() && price_to>=i.getPrice()){
                        result.add(i);
                    }
                }
                else if(!name.equals("null") && price_from==0 && price_to!=0 && brand.equals("null")){
                    if (i.getName().toLowerCase().contains(name.toLowerCase()) && price_to>=i.getPrice()){
                        result.add(i);
                    }
                }
                else if(name.equals("null") && price_from!=0 && price_to==0 && brand.equals("null")){
                    if (price_from<=i.getPrice()){
                        result.add(i);
                    }
                }
                else if(name.equals("null") && price_from==0 && price_to!=0 && brand.equals("null")){
                    if (price_to>=i.getPrice()){
                        result.add(i);
                    }
                }
                else if(name.equals("null") && price_from!=0 && price_to!=0 && brand.equals("null")){
                    if (price_from<=i.getPrice() && price_to>=i.getPrice()){
                        result.add(i);
                    }
                }
                else if(name.equals("null") && price_from==0 && price_to==0 && !brand.equals("null")){
                    System.out.println(i.getName()+" 1 brand");
                    if(i.getBrand().getName().toLowerCase().contains(brand.toLowerCase())){
                        System.out.println(i.getName()+" 2 brand founded");
                        result.add(i);
                    }
                }
                else if(name.equals("null") && price_from!=0 && price_to==0 && !brand.equals("null")){
                    if(i.getBrand().getName().toLowerCase().contains(brand.toLowerCase()) && price_from<=i.getPrice()){
                        System.out.println(i.getName()+" 2 brand price from");
                        result.add(i);
                    }
                }

                else if(name.equals("null") && price_from==0 && price_to!=0 && !brand.equals("null")){
                    if(i.getBrand().getName().toLowerCase().contains(brand.toLowerCase()) && price_to>=i.getPrice()){
                        System.out.println(i.getName()+" 2 brand price to");
                        result.add(i);
                    }
                }

                else if(name.equals("null") && price_from!=0 && price_to!=0 && !brand.equals("null")){
                    if(i.getBrand().getName().toLowerCase().contains(brand.toLowerCase())&&price_from<=i.getPrice() && price_to>=i.getPrice()){
                        System.out.println(i.getName()+" 2 brand price to and from");
                        result.add(i);
                    }
                }
            }
        }
        if(option.equals("asc") && search_brand.equals("null") && brand.equals("null")){
            System.out.println("if 1 asc");
            //List<Item> l = itemService.findAllByBrand_Name(brand);
            founded_items.sort(Comparator.comparing(Item::getPrice));
            for (Item i : founded_items) {
                System.out.println(i.getName() + " asc");
            }
            result.addAll(founded_items);
        }
        if(option.equals("desc") && search_brand.equals("null") && brand.equals("null")){
            System.out.println("if 1 asc");
            //List<Item> l = itemService.findAllByBrand_Name(brand);
            founded_items.sort(Comparator.comparing(Item::getPrice));
            for (Item i : founded_items) {
                System.out.println(i.getName() + " asc");
            }
            result.addAll(founded_items);
        }
        if(option.equals("asc") && search_brand.equals("null")){
            System.out.println("if 1 asc");
            List<Item> l = itemService.findAllByBrand_Name(brand);
            l.sort(Comparator.comparing(Item::getPrice));
            for (Item i : founded_items) {
                System.out.println(i.getName() + " asc");
            }
            result.addAll(l);
        }
         if(option.equals("desc") && search_brand.equals("null")){
             List<Item> l = itemService.findAllByBrand_Name(brand);
            l.sort(Comparator.comparing(Item::getPrice).reversed());
            for(Item i: founded_items){
                System.out.println(i.getName() + " desc");
            }
            result.addAll(l);
        }
        if(option.equals("asc") && !search_brand.equals("null")){
            System.out.println("if 2 asc");
            list.sort(Comparator.comparing(Item::getPrice));
            for (Item i : founded_items) {
                System.out.println(i.getName() + " asc");
            }
            result.addAll(list);
        }
        if(option.equals("desc") && !search_brand.equals("null")){
            System.out.println("if desc 2");
            list.sort(Comparator.comparing(Item::getPrice).reversed());
            for (Item i : founded_items) {
                System.out.println(i.getName() + " asc");
            }
            result.addAll(list);
        }
        if(option.equals("asc") && !brand.equals("null") && price_to!=0 && price_from==0){
            List<Item> l = new ArrayList<>();
            for(Item item:list){
                if(item.getPrice()<=price_to){
                    l.add(item);
                }
            }
            l.sort(Comparator.comparing(Item::getPrice));
            result.addAll(l);
        }
        model.addAttribute("brand",search_brand);
        model.addAttribute("founded_items",result);
        return "search_page";
    }

    @GetMapping(value = "/country")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String country(Model model){
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        List<Country> countries = itemService.getAllCountries();
        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("countries",countries);
        model.addAttribute("brands",brands);
        return "country";
    }

    @GetMapping(value = "/brand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String brand(Model model){
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        List<Country> countries = itemService.getAllCountries();
        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("countries",countries);
        model.addAttribute("brands",brands);
        return "brand";
    }

    @PostMapping(value = "/add_brand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String add_country(@RequestParam (name = "name") String name,
                              @RequestParam (name = "country") Long id){
        Country country = itemService.getCountryById(id);
        if(country!=null){
            itemService.addBrand(new Brand(null,name,country));
        }
        return "redirect:/country";
    }

    @PostMapping(value = "/edit_brand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit_brand(@RequestParam (name = "name") String name,
                             @RequestParam(name = "country") Long country_id,
                             @RequestParam(name = "brand_id") Long brand_id,
                             @RequestParam(name = "edit_btn",defaultValue = "null") String btn){
        if(btn.equals("edit")){
            Brand brand = itemService.getBrandById(brand_id);
            Country country = itemService.getCountryById(country_id);
            if(brand!=null && country!=null){
                brand.setName(name);
                brand.setCountry(country);
                itemService.updateBrand(brand);
            }
        }
        return "redirect:/country";
    }

    @PostMapping(value = "/delete_brand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete_brand(@RequestParam (name = "brand_id") Long brand_id){
        Brand brand = itemService.getBrandById(brand_id);
        if(brand!=null){
            itemService.deleteBrand(brand);
        }
        return "redirect:/country";
    }

    @PostMapping(value = "/add_country")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String add_brand(@RequestParam (name = "name") String name,
                              @RequestParam (name = "code") String code){
        itemService.addCountry(new Country(null,name,code));
        return "redirect:/country";
    }

    @PostMapping(value = "/edit_country")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit_brand(@RequestParam (name = "name") String name,
                             @RequestParam (name = "code") String code,
                             @RequestParam(name = "country_id") Long country_id){
        Country country = itemService.getCountryById(country_id);
        if(country!=null){
            country.setName(name);
            country.setCode(code);
            itemService.updateCountry(country);
        }
        return "redirect:/country";
    }

    @PostMapping(value = "/delete_country")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete_country(@RequestParam(name = "country_id") Long country_id){
        Country country = itemService.getCountryById(country_id);
        if(country!=null){
            itemService.deleteCountry(country);
        }
        return "redirect:/country";
    }

    @GetMapping(value = "/category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String category(Model model){
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        List<Category> categories = itemService.getAllCategories();
        model.addAttribute("categories",categories);
        return "category";
    }

    @PostMapping(value = "/add_category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String add_category(@RequestParam (name = "name") String name,
                               @RequestParam (name = "logo") String logo,Model model){
        model.addAttribute("basketSize",getBasketSize(session));
        itemService.addCategory(new Category(null,name,logo));
        return "redirect:/category";
    }

    @PostMapping(value = "/edit_category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit_category(@RequestParam (name = "name") String name,
                                @RequestParam (name = "logoURL") String logoURL,
                                @RequestParam(name = "category_id") Long category_id){
        Category category = itemService.getCategoryById(category_id);
        if(category!=null){
            category.setName(name);
            category.setLogoURL(logoURL);
            itemService.updateCategory(category);
        }
        return "redirect:/category";
    }

    @PostMapping(value = "/delete_category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete_category(@RequestParam(name = "category_id") Long category_id){
        Category category = itemService.getCategoryById(category_id);
        if(category!=null){
            itemService.deleteCategory(category);
        }
        return "redirect:/category";
    }

    @GetMapping(value = "/403")
    public String accessDenied(Model model){
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        return "403";
    }

    @GetMapping(value = "/login")
    public String login(Model model){
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        return "login";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model,@RequestParam(required = false) String success,
                          @RequestParam(required = false) String successAva,
                          @RequestParam(required = false)String deleted,
                          @RequestParam(required = false) String successPass,
                          @RequestParam(required = false) String not_same,
                          @RequestParam(required = false) String errorPass){
        model.addAttribute("success",success);
        model.addAttribute("successAva",successAva);
        model.addAttribute("deleted",deleted);

        model.addAttribute("successPass",successPass);
        model.addAttribute("not_same",not_same);
        model.addAttribute("errorPass",errorPass);

        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        model.addAttribute("brands",itemService.getAllBrands());
        return "profile";
    }

    private User getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            org.springframework.security.core.userdetails.User secUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            User user = userService.getUserByEmail(secUser.getUsername());
            return user;
        }
        return null;
    }

    private int getBasketSize(HttpSession session){
        List<Basket> baskets = (List<Basket>)session.getAttribute("basket");
        int size = 0;
        if(baskets==null)
            return 0;
        else {
            for(int i= 0;i<baskets.size();i++){
                if(baskets.get(i).getItems()!=null){
                   size+=baskets.get(i).getAmount();
                }
            }
        }
        return size;
    }

    @GetMapping(value = "/register")
    public String register(Model model,@RequestParam(required = false)String alert1,
                           @RequestParam(required = false) String alert2){
        model.addAttribute("success",alert1);
        model.addAttribute("error",alert2);
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        return "register";
    }

    @PostMapping(value = "/register")
    public String signUp(@RequestParam(name = "user_email") String user_email,
                         @RequestParam(name = "user_password") String user_password,
                         @RequestParam(name = "re_user_password") String re_user_password,
                         @RequestParam(name = "user_full_name") String user_full_name){
        String redirect = "";
        if(user_password.equals(re_user_password)){
            User user = new User();
            user.setEmail(user_email);
            user.setFullName(user_full_name);
            user.setPassword(user_password);
            if(userService.createUser(user)!=null){
                redirect = "success";
                return "redirect:/profile?";
            }
        }
        redirect = "error";
        return "redirect:/register?"+redirect;
    }

    @PostMapping(value = "/update_profile")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public String update_profile(@RequestParam(name = "user_full_name") String user_full_name,
                                 @RequestParam(name = "id") Long id){
       User user = userService.getUserById(id);
       if(user!=null){
           user.setFullName(user_full_name);
           userService.updateUser(user);
       }
       return "redirect:/profile?success";
    }

    @PostMapping(value = "/update_password")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String update_password(@RequestParam(name = "old_password") String old_password,
                                  @RequestParam(name = "new_password") String new_password,
                                  @RequestParam(name = "re_new_password") String re_new_password,
                                  @RequestParam(name = "id") Long id){
        String redirect = "";
        User user = userService.getUserById(id);
        if(user!=null){
            if(userService.changePassword(user,old_password,new_password,re_new_password)!=null){
                redirect = "successPass";
                return "redirect:/profile?" + redirect;
            }
            else {
                redirect = "not_same";
                return "redirect:/profile?"+redirect;
            }
        }
        redirect = "errorPass";
        return "redirect:/profile?"+ redirect;
    }

    @PostMapping(value = "/edit_user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit_user(@RequestParam(name = "id") Long id,
                            @RequestParam(name = "user_name") String user_name,
                            @RequestParam(name = "user_password") String user_password){
        String prof = update_profile(user_name,id);
        User user = userService.getUserById(id);
        if(user!=null && prof.equals("redirect:/profile?success")){
            userService.editPassword(user,user_password);
            return "redirect:/users";
        }
        return null;
    }

    @PostMapping(value = "/delete_user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete_user(@RequestParam(name = "id") Long id){
        User user = userService.getUserById(id);
        if(user!=null){
            userService.deleteUser(user);
        }
        return "redirect:/users";
    }

    @GetMapping(value = "/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String users(Model model){
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        List<User> users = userService.getAllUsers();
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("users",users);
        model.addAttribute("roles",roles);
        return "users";
    }

    @GetMapping(value = "/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String roles(Model model){
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles",roles);
        return "roles";
    }

    @PostMapping(value = "/add_role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String add_roles(@RequestParam(name = "role") String role,
                            @RequestParam(name = "description") String description){
        roleService.addRole(new Role(null,role,description));
        return "redirect:/roles";
    }

    @PostMapping(value = "/edit_role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit_role(@RequestParam(name = "role") String role,
                            @RequestParam(name = "description") String description,
                            @RequestParam(name = "id") Long id){
        Role r = roleService.getRoleById(id);
        if(r!=null){
            r.setRole(role);
            r.setDescription(description);
            roleService.updateRole(r);
        }
        return "redirect:/roles";
    }

    @PostMapping(value = "/delete_role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete_role(@RequestParam(name = "id") Long id){
        Role role = roleService.getRoleById(id);
        if(role!=null){
            roleService.deleteRole(role);
        }
        return "redirect:/roles";
    }

    @GetMapping(value = "/edit_user_details/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit_user_details(Model model,@PathVariable(name = "id") Long id){
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        User user = userService.getUserById(id);
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("user",user);
        model.addAttribute("roles",roles);
        return "details_users";
    }

    @PostMapping(value = "/set_role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String set_role(@RequestParam(name = "user_id") Long user_id,
                           @RequestParam(name = "role_id") Long role_id,
                           @RequestParam(name = "btn") String btn,Model model){
        model.addAttribute("currentUser",getUserData());
        Role role = roleService.getRoleById(role_id);
        if(role!=null){
            User user = userService.getUserById(user_id);
            if(user!=null){
                List<Role> roles = user.getRoles();
                if(btn.equals("set")){
                    if(roles==null){
                        roles = new ArrayList<>();
                    }
                    roles.add(role);
                    userService.updateUser(user);
                }
                if(btn.equals("unset")){
                    roles.remove(role);
                    userService.updateUser(user);
                }
            }
        }
        return "redirect:/edit_user_details/"+user_id+"#rolesDiv";
    }

    @PostMapping(value = "/upload_avatar")
    @PreAuthorize("isAuthenticated()")
    public String upload_avatar(@RequestParam(name = "user_img")MultipartFile file,@RequestParam(name = "btn") String btn,
                                @RequestParam(name = "user_id")Long user_id){
        String redirect = "";
        if((file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) && btn.equals("upload")) {
            try {
                User user = getUserData();
                String picName = DigestUtils.sha1Hex("avatar_"+user.getId()+"_picture");
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName+".jpg");
                Files.write(path, bytes);
                user.setUserAvatar(picName);
                userService.updateUser(user);
                redirect = "profile?successAva";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(btn.equals("deleteAva")){
            User user = userService.getUserById(user_id);
            user.setUserAvatar(null);
            userService.updateUser(user);
            redirect = "profile?deleted";
        }
        return "redirect:/" + redirect;
    }

    @PostMapping(value = "/upload_picture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String upload_picture(@RequestParam(name = "item_img") MultipartFile file,@RequestParam(name = "item_id") Long id){
        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")){
            try {
                //User user = getUserData();
                Item item = itemService.getItemById(id);
                List<Picture> pictures = pictureService.findAllByItem_Id(id);
                String picName = DigestUtils.sha1Hex("picture_"+Timestamp.valueOf(LocalDateTime.now())+"_item");
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPathItem+picName+".jpg");
                Files.write(path,bytes);
                Picture picture = new Picture(null,picName,Timestamp.valueOf(LocalDateTime.now()),item);
                pictureService.addPicture(picture);
                if(pictures==null){
                    pictures = new ArrayList<>();
                }
                pictures.add(picture);
                pictureService.updatePicture(picture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/edit_details/"+id+"#picturesDiv";
    }

    @GetMapping(value = "/view_photo/{url}",produces = {MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] viewProfilePhoto(@PathVariable(name = "url") String url,Model model) throws IOException{
        model.addAttribute("basketSize",getBasketSize(session));
        String pictureUrl = viewPath+defaultPicture;
        if(url!=null && !url.equals("null")){
            pictureUrl = viewPath+url+".jpg";
        }
        InputStream in;
        try {
            ClassPathResource resource = new ClassPathResource(pictureUrl);
            in = resource.getInputStream();
        }catch (Exception e){
            ClassPathResource resource = new ClassPathResource(viewPath+defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/view_item/{url}",produces = {MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] viewItemPhoto(@PathVariable(name = "url") String url,Model model) throws IOException{
        model.addAttribute("basketSize",getBasketSize(session));
        String pictureUrl = viewPathItem+defaultPictureItem;
        if(url!=null && !url.equals("null")){
            pictureUrl = viewPathItem+url+".jpg";
        }
        InputStream in;
        try {
            ClassPathResource resource = new ClassPathResource(pictureUrl);
            in = resource.getInputStream();
        }catch (Exception e){
            ClassPathResource resource = new ClassPathResource(viewPathItem+defaultPictureItem);
            in = resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }

    @PostMapping(value = "/unset_picture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String unset_picture(@RequestParam(name = "btn") String btn,
                                @RequestParam(name = "picture_id") Long picture_id,
                                @RequestParam(name = "item_id") Long id){
        Picture picture = pictureService.getPictureById(picture_id);
        if(picture!=null && btn.equals("unset")){
            pictureService.deletePicture(picture);
        }
        return "redirect:/edit_details/"+id+"#picturesDiv";
    }


    @GetMapping(value = "/basket")
    public String basket(Model model,HttpSession session){
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        //Basket basket = (Basket)session.getAttribute("basket");
        //List<Item> items = (List<Item>)session.getAttribute("basket");
        List<Basket> baskets = (List<Basket>)session.getAttribute("basket");
        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("brands",brands);
        model.addAttribute("basket",baskets);
        int sum = 0;
        if(baskets!=null) {
            for (int i = 0; i < baskets.size(); i++) {
                sum += baskets.get(i).getAmount() * baskets.get(i).getItems().getPrice();
            }
        }
        model.addAttribute("total",sum);
        return "basket";
    }

    private int exist(Long id,List<Basket> items){
        //List<Item> items = basket.getItems();
        for(int i = 0;i<items.size();i++){
            if(items.get(i).getItems().getId().equals(id)){
                return i;
            }
        }
        return -1;
    }

    private int calc(HttpSession session,Model model){
        int sum = 0;
        List<Basket> baskets = (List<Basket>)session.getAttribute("basket");
        if(baskets!=null){
            for(int i = 0;i<baskets.size();i++){
                sum+=baskets.get(i).getAmount()*baskets.get(i).getItems().getPrice();
            }
        }
        //model.addAttribute("total",sum);
        return sum;
    }

    @PostMapping(value = "/add_to_basket")
    public String add_to_basket(Model model, @RequestParam(name = "item_id") Long item_id,
                                HttpSession session, HttpServletRequest request, HttpServletResponse response){
        Item item = itemService.getItemById(item_id);
        Basket b = null;
        Cookie [] cookies = request.getCookies();
        if(session.getAttribute("basket")==null){
             b = new Basket(item,1);
//            List<Item> items = new ArrayList<>();
//            items.add(item);
//            session.setAttribute("basket",items);
            List<Basket> baskets = new ArrayList<>();
            baskets.add(b);
            session.setAttribute("basket",baskets);
            for(Cookie c: cookies){
                if(c.getName().equals("JSESSIONID")){
                    c.setMaxAge(60*60);
                    response.addCookie(c);
                    break;
                }
            }
        }
        else {
            //Basket basket = (Basket)session.getAttribute("basket");
//            List<Item> items = (List<Item>) session.getAttribute("basket");
//            int ind = exist(item_id,items);
//            if(ind==-1){
//                items.add(item);
//            }
//            else {
//                //amount ++
//                amount++;
//            }
//            session.setAttribute("basket",items);
            List<Basket> baskets = (List<Basket>)session.getAttribute("basket");
            int ind = exist(item_id,baskets);
            if(ind==-1){
                baskets.add(new Basket(item,1));
            }
            else {
                int amount = baskets.get(ind).getAmount()+1;
                baskets.get(ind).setAmount(amount);
            }
            session.setAttribute("basket",baskets);

            for(Cookie c: cookies){
                if(c.getName().equals("JSESSIONID")){
                    c.setMaxAge(60*60);
                    response.addCookie(c);
                    break;
                }
            }
        }
        return "redirect:/";
    }

    @PostMapping(value = "/set_amount")
    public String set_amount(@RequestParam(name = "item_id") Long item_id,
                             @RequestParam(name = "basket_id") int basket_id,HttpSession session,
                             @RequestParam(name = "btn")String btn){
        List<Basket> baskets = (List<Basket>)session.getAttribute("basket");
        int ind = exist(item_id,baskets);
        if(btn.equals("increase")){
            int amount = baskets.get(ind).getAmount()+1;
            baskets.get(ind).setAmount(amount);
        }
        else {
            int amount = baskets.get(ind).getAmount()-1;
            baskets.get(ind).setAmount(amount);
        }
//        Basket b = new Basket();
//        int total = b.calculate(baskets);
        return "redirect:/basket";
    }

    @PostMapping(value = "/manage_basket")
    public String manage_basket(@RequestParam(name = "btn") String btn,HttpSession session,
                                Model model,HttpServletResponse response,HttpServletRequest request){
        List<Basket> baskets = (List<Basket>)session.getAttribute("basket");
        Cookie [] cookies = request.getCookies();
        if(btn.equals("clear")){
            baskets = null;
            for(Cookie c: cookies){
                if(c.getName().equals("JSESSIONID")){
                    c.setMaxAge(0);
                    break;
                }
            }
            session.removeAttribute("basket");
        }
        model.addAttribute("basket",baskets);
        return "redirect:/basket";
    }

    @GetMapping(value = "/purchase_details")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String purchase_details(Model model){
        model.addAttribute("basketSize",getBasketSize(session));
        model.addAttribute("currentUser",getUserData());
        List<PurchaseDetail> purchaseDetails = purchaseDetailService.getAllPurchaseDetails();
        model.addAttribute("purchaseDetails",purchaseDetails);
        return "purchase_details";
    }

    @PostMapping(value = "/order_data")
    public String order_data(HttpSession session){
        List<Basket> baskets = (List<Basket>)session.getAttribute("basket");
        for (Basket basket : baskets) {
            purchaseDetailService.addPurchaseDetail(new PurchaseDetail(null, Timestamp.valueOf(LocalDateTime.now()), basket.getAmount(), basket.getItems()));
        }
        session.removeAttribute("basket");
        return "redirect:/";
    }

    @PostMapping(value = "add_comment")
    @PreAuthorize("isAuthenticated()")
    public String add_comment(@RequestParam(name = "comment") String comment,
                              @RequestParam(name = "item_id") Long item_id){
        User user = getUserData();
        Item item = itemService.getItemById(item_id);
        if(comment!=null && item_id!=null){
            commentService.addComment(new Comment(null,comment,Timestamp.valueOf(LocalDateTime.now()),item,user));
        }
        return "redirect:/details/" + item_id + "#comment";
    }


    @PostMapping(value = "edit_comment")
    @PreAuthorize("isAuthenticated()")
    public String edit_comment(@RequestParam(name = "comment_id") Long comment_id,
                               @RequestParam(name = "comment") String comm,
                               @RequestParam(name = "item_id") Long item_id){
        Comment comment = commentService.getCommentById(comment_id);
        if(comment!=null){
            comment.setComment(comm);
            comment.setAddedDateComment(Timestamp.valueOf(LocalDateTime.now()));
            commentService.updateComment(comment);
        }
        return "redirect:/details/" + item_id + "#comment";
    }

    @PostMapping(value = "delete_comment")
    @PreAuthorize("isAuthenticated()")
    public String delete_comment(@RequestParam(name = "comment_id") Long comment_id,
                               @RequestParam(name = "comment") String comm,
                               @RequestParam(name = "item_id") Long item_id){
        Comment comment = commentService.getCommentById(comment_id);
        if(comment!=null){
            commentService.deleteComment(comment);
        }
        return "redirect:/details/" + item_id + "#comment";
    }

    private ISpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addDialect(new Java8TimeDialect());
        engine.setTemplateResolver(templateResolver);
        return engine;
    }
}


