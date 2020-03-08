package xyz.vaith.weeblogbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Page<T> {
    private Integer size;
    private Integer total;
    private Integer currentPage;
    private Integer totalPage;
    private List<T> data;
}
