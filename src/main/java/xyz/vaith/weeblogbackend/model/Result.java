package xyz.vaith.weeblogbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    boolean result = true;
    int code = 0;
    String message = "success";
    Object data;

    public static Result success(Object data) {
        Result result = new Result();
        result.data = data;
        return result;
    }

    public static Result defaultFail() {
        Result result = new Result();
        result.message = "系统错误";
        result.result = false;
        result.code = -1;
        return result;
    }

    public static Result fail(String message) {
        Result result = new Result();
        result.message = message;
        result.result = false;
        result.code = 1001;
        return result;
    }
}
