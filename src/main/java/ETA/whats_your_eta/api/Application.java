package ETA.whats_your_eta.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // @CreatedDate 사용 위해 추가
@EnableScheduling
public class Application {
	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true"); //local에서만 추가
	}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
