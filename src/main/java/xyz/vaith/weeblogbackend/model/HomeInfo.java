package xyz.vaith.weeblogbackend.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * home_info
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeInfo implements Serializable {
    private Integer id;

    private Integer coverId;

    private String greeting;

    private Date createDate;

    private Date updateDate;

    private Image cover;

    private static final long serialVersionUID = 1L;
}