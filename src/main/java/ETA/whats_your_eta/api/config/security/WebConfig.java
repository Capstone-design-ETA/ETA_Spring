package ETA.whats_your_eta.api.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("/*") // 외부에서 들어오는 모든 url 허용
                .allowedMethods("*") // GET, POST, PUT, PATCH, DELETE, OPTIONS 등의 메소드 허용
                .allowedHeaders("*"); // 허용되는 헤더
    }
}
