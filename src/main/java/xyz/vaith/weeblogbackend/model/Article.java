package xyz.vaith.weeblogbackend.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * article
 * @author 
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article implements Serializable {
    private Integer id;

    private String content;

    private Date createDate;

    private Date updateDate;

    private Category category;

    private List<Tag> tags;

    private static final long serialVersionUID = 1L;
}