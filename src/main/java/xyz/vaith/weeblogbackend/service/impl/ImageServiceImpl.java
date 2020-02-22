package xyz.vaith.weeblogbackend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.vaith.weeblogbackend.mapper.ImageMapper;
import xyz.vaith.weeblogbackend.model.Image;
import xyz.vaith.weeblogbackend.service.ImageService;
import xyz.vaith.weeblogbackend.util.QiniuUtil;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
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
        Long length = file.getSize();

        File target = new File(tmp, nfn);
        file.transferTo(target);

        String key  = QiniuUtil.uploadFile(target);
        BufferedImage bi = ImageIO.read(new FileInputStream(target));
        Image image = Image.builder().name(nfn).contentType(contentType).length(length).server(1).bucket("image").key(key).width((double) bi.getWidth()).heigth((double) bi.getHeight()).originalName(filename).build();
        imageMapper.insert(image);
        image.setPreviewURL(QiniuUtil.getLimitURL(key, QiniuUtil.preview));

        return image;
    }

    @Override
    public List<Image> getImageList(int page, int size) throws Exception {
        List<Image> images = imageMapper.selectImageList(page * size, size);
        for (Image image : images) {
            image.setPreviewURL(QiniuUtil.getLimitURL(image.getKey(), QiniuUtil.preview));
        }
        return images;
    }
}

