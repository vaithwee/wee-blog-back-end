package xyz.vaith.weeblogbackend.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    private Double heigth;

    private Integer server;

    private String bucket;

    private String previewURL;

    private String originalURL;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        this.previewURL = QiniuUtil.getLimitURL(key, QiniuUtil.preview);
        this.originalURL = QiniuUtil.getOrininalURL(key);
    }

    private static final long serialVersionUID = 1L;
}
