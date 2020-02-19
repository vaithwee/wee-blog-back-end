package xyz.vaith.weeblogbackend.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * article_tag
 * @author 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTag implements Serializable {
    private Integer id;

    private Integer articleId;

    private Integer tagId;

    private Date createDate;

    private Date updateDate;

    private static final long serialVersionUID = 1L;
}