package eshop.Home_Task_7_spring.services.impl;

import eshop.Home_Task_7_spring.entities.PurchaseDetail;
import eshop.Home_Task_7_spring.repositories.PurchaseDetailRepository;
import eshop.Home_Task_7_spring.services.PurchaseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseDetailServiceImpl implements PurchaseDetailService {

    @Autowired
    private PurchaseDetailRepository purchaseDetailRepository;

    @Override
    public PurchaseDetail addPurchaseDetail(PurchaseDetail purchaseDetail) {
        return purchaseDetailRepository.save(purchaseDetail);
    }

    @Override
    public List<PurchaseDetail> getAllPurchaseDetails() {
        return purchaseDetailRepository.findAll();
    }

    @Override
    public PurchaseDetail getPurchaseDetailById(Long id) {
        return purchaseDetailRepository.getOne(id);
    }

    @Override
    public void deletePurchaseDetail(PurchaseDetail purchaseDetail) {
        purchaseDetailRepository.delete(purchaseDetail);
    }

    @Override
    public PurchaseDetail updatePurchaseDetail(PurchaseDetail purchaseDetail) {
        return purchaseDetailRepository.save(purchaseDetail);
    }
}
