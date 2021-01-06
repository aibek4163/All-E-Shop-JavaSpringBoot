package eshop.Home_Task_7_spring.services;

import eshop.Home_Task_7_spring.entities.PurchaseDetail;

import java.util.List;

public interface PurchaseDetailService {
    PurchaseDetail addPurchaseDetail(PurchaseDetail purchaseDetail);
    List<PurchaseDetail> getAllPurchaseDetails();
    PurchaseDetail getPurchaseDetailById(Long id);
    void deletePurchaseDetail(PurchaseDetail purchaseDetail);
    PurchaseDetail updatePurchaseDetail(PurchaseDetail purchaseDetail);
}
