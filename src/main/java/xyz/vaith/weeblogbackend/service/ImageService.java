package xyz.vaith.weeblogbackend.service;

import org.springframework.web.multipart.MultipartFile;
import xyz.vaith.weeblogbackend.model.Image;
import xyz.vaith.weeblogbackend.model.Page;

import java.util.List;

public interface ImageService {
    Image saveImageFileToBucket(MultipartFile file, String filename, String tmp) throws Exception;
    Page getImageList(int page, int size) throws Exception;
}
