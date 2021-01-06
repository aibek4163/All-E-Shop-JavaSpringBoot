package eshop.Home_Task_7_spring.services.impl;

import eshop.Home_Task_7_spring.entities.Picture;
import eshop.Home_Task_7_spring.repositories.PictureRepository;
import eshop.Home_Task_7_spring.services.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PictureServiceImpl implements PictureService {

    @Autowired
    private PictureRepository pictureRepository;

    @Override
    public Picture addPicture(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public List<Picture> getAllPictures() {
        return pictureRepository.findAll();
    }

    @Override
    public Picture getPictureById(Long id) {
        return pictureRepository.getOne(id);
    }

    @Override
    public void deletePicture(Picture picture) {
        pictureRepository.delete(picture);
    }

    @Override
    public Picture updatePicture(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public List<Picture> findAllByItem_Id(Long id) {
        return pictureRepository.findAllByItem_Id(id);
    }
}
