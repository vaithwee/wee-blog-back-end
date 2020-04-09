package xyz.vaith.weeblogbackend.service.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.cache.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.vaith.weeblogbackend.mapper.ImageMapper;
import xyz.vaith.weeblogbackend.model.Image;
import xyz.vaith.weeblogbackend.model.Page;
import xyz.vaith.weeblogbackend.redis.RedisCacheKeys;
import xyz.vaith.weeblogbackend.service.ImageService;
import xyz.vaith.weeblogbackend.util.QiniuToken;
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
@Log4j2
public class ImageServiceImpl implements ImageService {
    @Resource
    ImageMapper imageMapper;

    @Resource
    QiniuToken token;

    @Override
    @CacheEvict(value = RedisCacheKeys.IMAGE_LIST, allEntries = true)
    public Image saveImageFileToBucket(MultipartFile file, String filename, String tmp) throws Exception {

        log.info(token.getSecretKey());

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

        if (filename == null || filename.length() == 0) {
            filename = file.getOriginalFilename().replace("." + ext, "");
        }

        File target = new File(tmp, nfn);
        file.transferTo(target);

        String key  = QiniuUtil.defaultUtil().uploadFile(target);
        BufferedImage bi = ImageIO.read(new FileInputStream(target));
        Image image = Image.builder().name(nfn).contentType(contentType).length(length).server(1).bucket("image").key(key).width((double) bi.getWidth()).heigth((double) bi.getHeight()).originalName(filename).build();
        imageMapper.insert(image);
        image.setPreviewURL(QiniuUtil.defaultUtil().getLimitURL(key, QiniuUtil.preview));
        image.setOriginalURL(QiniuUtil.defaultUtil().getOriginalURL(key));

        return image;
    }

    @Override
    @Cacheable(value = RedisCacheKeys.IMAGE_LIST, key = "#page")
    public Page<Image> getImageList(int page, int size) throws Exception {
        log.info("图片缓存生成");
        List<Image> images = imageMapper.selectImageList(page * size, size);
        int total = imageMapper.selectCount();
        int totalPage = total%size == 0 ? total/size : total/size + 1;
        return Page.<Image>builder().data(images).size(size).currentPage(page).total(total).totalPage(totalPage).build();
    }

    @Override
    @CacheEvict(value = RedisCacheKeys.IMAGE_LIST, allEntries = true)
    public void deleteImage(int id) throws Exception {
        imageMapper.deleteByPrimaryKey(id);
    }
}

