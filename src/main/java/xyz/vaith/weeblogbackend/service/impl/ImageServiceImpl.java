package xyz.vaith.weeblogbackend.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.vaith.weeblogbackend.enumerate.image.ImageBucketType;
import xyz.vaith.weeblogbackend.enumerate.image.ImageServerType;
import xyz.vaith.weeblogbackend.enumerate.util.EnumUtils;
import xyz.vaith.weeblogbackend.enumerate.image.ImageAccessType;
import xyz.vaith.weeblogbackend.exception.BuzzException;
import xyz.vaith.weeblogbackend.mapper.ArticleCoverMapper;
import xyz.vaith.weeblogbackend.mapper.HomeInfoMapper;
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
import java.util.Date;
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
    ArticleCoverMapper articleCoverMapper;

    @Resource
    HomeInfoMapper homeInfoMapper;

    @Resource
    QiniuToken token;

    @Override
    @CacheEvict(value = RedisCacheKeys.IMAGE_LIST, allEntries = true)
    public Image saveImageFileToBucket(MultipartFile file, String filename, String tmp, Integer type) throws Exception {

        log.info(token.getSecretKey());

        File dir = new File(tmp);
        System.out.println(tmp);
        if (!dir.exists()) {
            boolean b = dir.mkdir();
            if (!b) {
                throw new IOException();
            }
        }

        String uuid = UUID.randomUUID().toString().replace(" ", "").toLowerCase();
        ;
        String ext = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().indexOf("."));
        String nfn = uuid + ext;
        String contentType = file.getContentType();
        Long length = file.getSize();

        if (filename == null || filename.length() == 0) {
            filename = file.getOriginalFilename().replace("." + ext, "");
        }

        File target = new File(tmp, nfn);
        file.transferTo(target);

        ImageAccessType accessType = EnumUtils.codeOf(ImageAccessType.class, type);
        ImageBucketType bucketType;
        switch (accessType) {
            case PUBLIC:
                bucketType = ImageBucketType.MARKDOWN;
                break;
            default:
                bucketType = ImageBucketType.IMAGES;
                break;
        }


        String key = QiniuUtil.defaultUtil().uploadFile(target, accessType);
        BufferedImage bi = ImageIO.read(new FileInputStream(target));

        Image image = Image.builder().createDate(new Date())
                .updateDate(new Date())
                .name(nfn)
                .contentType(contentType)
                .length(length)
                .server(ImageServerType.QINIU)
                .bucket(bucketType)
                .key(key)
                .width( bi.getWidth())
                .height(bi.getHeight())
                .type(EnumUtils.codeOf(ImageAccessType.class, type))
                .originalName(filename).build();
        imageMapper.insert(image);
        return image;
    }

    @Override
    @Cacheable(value = RedisCacheKeys.IMAGE_LIST, key = "#page+ '-' + #size")
    public Page<Image> getImageList(int page, int size) throws Exception {
        log.info("图片缓存生成");
        List<Image> images = imageMapper.selectImageList(page * size, size);
        int total = imageMapper.selectCount();
        int totalPage = total % size == 0 ? total / size : total / size + 1;
        return Page.<Image>builder().data(images).size(size).currentPage(page).total(total).totalPage(totalPage).build();
    }

    @Override
    @CacheEvict(value = RedisCacheKeys.IMAGE_LIST, allEntries = true)
    public void deleteImage(int id) throws Exception {
        Image image = imageMapper.selectByPrimaryKey(id);
        if (image != null) {
            articleCoverMapper.deleteByImageId(image.getId());
            homeInfoMapper.deleteByImageId(image.getId());
            imageMapper.deleteByPrimaryKey(image.getId());
            QiniuUtil.defaultUtil().delete(image.getKey(), image.getType());
        } else  {
            throw new BuzzException("图片ID不存在");
        }


    }
}

