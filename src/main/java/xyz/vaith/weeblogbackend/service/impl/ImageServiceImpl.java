package xyz.vaith.weeblogbackend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.vaith.weeblogbackend.mapper.ImageMapper;
import xyz.vaith.weeblogbackend.model.Image;
import xyz.vaith.weeblogbackend.service.ImageService;
import xyz.vaith.weeblogbackend.util.QiniuUtil;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {
    @Resource
    ImageMapper imageMapper;

    @Override
    public Image saveImageFileToBucket(MultipartFile file, String filename, String tmp) throws Exception {

        File dir = new File(tmp);
        System.out.println(tmp);
        if (!dir.exists()) {
            boolean b = dir.mkdir();
            if (!b) {
                throw new IOException();
            }
        }

        String uuid = UUID.randomUUID().toString().replace(" ", "").toLowerCase();;
        String ext = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().indexOf("."));
        String nfn = uuid + ext;
        String contentType = file.getContentType();
        int length = (int) file.getSize();

        File target = new File(tmp, nfn);
        file.transferTo(target);

        String key  = QiniuUtil.uploadFile(target);

        Image image = Image.builder().name(nfn).contentType(contentType).length(length).server(1).bucket("image").key(key).build();
        imageMapper.insert(image);


        return image;
    }
}

