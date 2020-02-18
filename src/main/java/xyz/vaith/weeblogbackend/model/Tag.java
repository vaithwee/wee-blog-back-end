package xyz.vaith.weeblogbackend.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tag
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag implements Serializable {
    private Integer id;

    private String name;

    private Integer type;

    private Integer sort;

    private Date createDate;

    private Date updateDate;

    private static final long serialVersionUID = 1L;
}
