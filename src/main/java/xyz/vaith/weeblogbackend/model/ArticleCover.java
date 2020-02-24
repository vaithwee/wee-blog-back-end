package xyz.vaith.weeblogbackend.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * article_cover
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleCover implements Serializable {
    private Integer id;

    private Integer articleId;

    private Integer imageId;

    private Date createDate;

    private Date updateDate;

    private static final long serialVersionUID = 1L;
}