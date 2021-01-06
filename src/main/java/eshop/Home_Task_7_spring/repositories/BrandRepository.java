package eshop.Home_Task_7_spring.repositories;

import eshop.Home_Task_7_spring.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface BrandRepository extends JpaRepository<Brand,Long> {
    List<Brand> findBrandsByNameContaining(String brand_name);
}
