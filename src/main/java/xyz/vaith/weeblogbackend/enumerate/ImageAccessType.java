package xyz.vaith.weeblogbackend.enumerate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ImageAccessType implements BaseEnum {
    PUBLIC(0, "公开"),
    PRIVATE(1, "私有");

    private Integer code;
    private String text;

    ImageAccessType(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
