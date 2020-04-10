package xyz.vaith.weeblogbackend.enumerate.image;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import xyz.vaith.weeblogbackend.enumerate.BaseEnum;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ImageServerType implements BaseEnum {
    QINIU(0, "七牛");
    private Integer code;
    private String text;

    ImageServerType(Integer code, String text) {
        this.code = code;
        this.text = text;
    }


}
