package ETA.whats_your_eta.api.config.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;


/**
 * Multipart 요청의 JSON 데이터를 처리하기 위한 커스텀 HTTP 메시지 컨버터
 * application/octet-stream 타입의 요청을 Jackson ObjectMapper를 사용해 자바 객체로 변환
 * 목적:
 * 1. Swagger UI에서 전송되는 multipart/form-data 요청 중 JSON 부분을 처리
 * 2. application/octet-stream으로 오는 요청을 Jackson을 사용해 자바 객체로 변환
 * 3. 응답 생성(write)은 처리하지 않고 요청 처리(read)만 담당
 */
@Component
public class MultipartJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return false;
    }
}