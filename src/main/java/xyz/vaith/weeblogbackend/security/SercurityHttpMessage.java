package xyz.vaith.weeblogbackend.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.io.InputStream;

@Data
@NoArgsConstructor
public class SercurityHttpMessage implements HttpInputMessage {

    private InputStream body;
    private HttpHeaders headers;


    public SercurityHttpMessage(InputStream body, HttpHeaders httpHeaders) {
        this.body = body;
        this.headers = httpHeaders;
    }

    @Override
    public InputStream getBody() {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
