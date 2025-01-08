package ETA.whats_your_eta.api.domain.diary.dto;

import ETA.whats_your_eta.api.domain.diary.Diary;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.time.LocalDate;

@Getter
public class DiaryRequestDto {

    private String location;

    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90.0")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90.0")
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180.0")
    private Double longitude;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // Spring이 요청 시 문자열을 LocalDate로 파싱
    private LocalDate date;

    private String content;

    // Dto -> Entity
    public Diary toEntity() {
        return Diary.builder()
                .location(this.location)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .date(this.date)
                .content(this.content)
                .build();
    }
}
