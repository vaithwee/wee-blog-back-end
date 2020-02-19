package xyz.vaith.weeblogbackend.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * article_category
 * @author 
 */
@Data
@Builder
public class ArticleCategory implements Serializable {
    private Integer id;

    private Integer articleId;

    private Integer categoryId;

    private Date createDate;

    private Date updateDate;

    private static final long serialVersionUID = 1L;
}