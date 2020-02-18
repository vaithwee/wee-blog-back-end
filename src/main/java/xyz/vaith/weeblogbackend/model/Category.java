package xyz.vaith.weeblogbackend.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;

/**
 * category
 * @author
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    private Integer id;

    private String name;

    private Date createDate = new Date();

    private Date updateDate = new Date();

    private static final long serialVersionUID = 1L;
}
