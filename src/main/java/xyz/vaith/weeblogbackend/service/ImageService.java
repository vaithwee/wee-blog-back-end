package xyz.vaith.weeblogbackend.service;

import org.springframework.web.multipart.MultipartFile;
import xyz.vaith.weeblogbackend.model.Image;
import xyz.vaith.weeblogbackend.model.Page;

import java.util.List;

public interface ImageService {
    Image saveImageFileToBucket(MultipartFile file, String filename, String tmp, Integer type) throws Exception;
    Page getImageList(int page, int size) throws Exception;
    void deleteImage(int id) throws Exception;
}
