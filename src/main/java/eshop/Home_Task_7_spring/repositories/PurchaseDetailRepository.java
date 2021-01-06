package eshop.Home_Task_7_spring.repositories;

import eshop.Home_Task_7_spring.entities.PurchaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PurchaseDetailRepository extends JpaRepository<PurchaseDetail,Long> {

}
