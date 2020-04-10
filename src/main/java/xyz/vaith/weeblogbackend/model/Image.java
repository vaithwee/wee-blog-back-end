package xyz.vaith.weeblogbackend.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.vaith.weeblogbackend.enumerate.image.ImageAccessType;
import xyz.vaith.weeblogbackend.enumerate.image.ImageBucketType;
import xyz.vaith.weeblogbackend.enumerate.image.ImageServerType;
import xyz.vaith.weeblogbackend.util.QiniuUtil;

/**
 * image
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image implements Serializable {
    private Integer id;

    private String name;

    private String originalName;

    private String key;

    private String contentType;

    private Long length;

    private Double width;

    private Double height;

    private ImageServerType server;

    private ImageBucketType bucket;

    private String previewURL;

    private String originalURL;

    private ImageAccessType type;

    private Date createDate;

    private Date updateDate;

    public String getPreviewURL() {
        if (previewURL == null) {
            previewURL = QiniuUtil.defaultUtil().getLimitURL(key, QiniuUtil.preview, type);
        }
        return previewURL;
    }

    public String getOriginalURL() {
        if (originalURL == null) {
            originalURL = QiniuUtil.defaultUtil().getOriginalURL(key, type);
        }
        return originalURL;
    }

    private static final long serialVersionUID = 1L;
}