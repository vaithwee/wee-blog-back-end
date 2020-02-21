package xyz.vaith.weeblogbackend.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String key;

    private String contentType;

    private Integer length;

    private Double width;

    private Double heigth;

    private Integer server;

    private String bucket;

    private static final long serialVersionUID = 1L;
}