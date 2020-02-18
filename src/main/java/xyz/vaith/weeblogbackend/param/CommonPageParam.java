package xyz.vaith.weeblogbackend.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonPageParam {
    int page;
    int size;
}
