package eshop.Home_Task_7_spring.services;

import eshop.Home_Task_7_spring.entities.Item;
import eshop.Home_Task_7_spring.entities.Picture;

import java.util.List;

public interface PictureService {
    Picture addPicture(Picture picture);
    List<Picture> getAllPictures();
    Picture getPictureById(Long id);
    void deletePicture(Picture picture);
    Picture updatePicture(Picture picture);

    List<Picture> findAllByItem_Id(Long id);
}
