package ETA.whats_your_eta.api.domain.diary.dto;

import ETA.whats_your_eta.api.domain.diary.Diary;
import lombok.Getter;

@Getter
public class DiaryRequestDto {

    private String location;
    private String date;
    private String content;

    // Dto -> Entity
    public Diary toEntity() {
        return Diary.builder()
                .location(this.location)
                .date(this.date)
                .content(this.content)
                .build();
    }
}
