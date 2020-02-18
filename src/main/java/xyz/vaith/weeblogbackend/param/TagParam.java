package xyz.vaith.weeblogbackend.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagParam {
    private Integer id;
    private String name;
    private Integer type;
    private Integer sort;
}
