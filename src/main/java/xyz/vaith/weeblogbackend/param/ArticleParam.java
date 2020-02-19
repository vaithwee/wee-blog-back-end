package xyz.vaith.weeblogbackend.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleParam {
    Integer id;
    Integer categoryID;
    List<Integer> tags;
    String content;
}
