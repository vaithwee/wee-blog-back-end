package xyz.vaith.weeblogbackend.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserParam {
    private String username;
    private String password;
    private String token;
}
