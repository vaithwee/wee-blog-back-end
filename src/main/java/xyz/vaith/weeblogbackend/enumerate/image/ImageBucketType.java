package xyz.vaith.weeblogbackend.enumerate.image;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import xyz.vaith.weeblogbackend.enumerate.BaseEnum;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ImageBucketType implements BaseEnum {

    IMAGES(0, "image"),
    MARKDOWN(1, "wee-markdown")
    ;

    private Integer code;
    private String text;

    ImageBucketType(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
