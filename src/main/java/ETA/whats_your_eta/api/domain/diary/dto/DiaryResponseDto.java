package ETA.whats_your_eta.api.domain.diary.dto;

import ETA.whats_your_eta.api.domain.diary.Diary;
import ETA.whats_your_eta.api.domain.image.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class DiaryResponseDto {

    @Builder
    static public class Info {
        private Long id;
        private String location;
        private String date;
        private String userName;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private String content;
        private List<String> imageUrls;
    }

    public static Info of(Diary diary) {
        return Info.builder()
                .id(diary.getId())
                .location(diary.getLocation())
                .date(diary.getDate())
                .userName(diary.getUser().getUsername())
                .createdAt(diary.getCreatedAt())
                .modifiedAt(diary.getModifiedAt())
                .content(diary.getContent())
                .imageUrls(diary.getImages().stream()
                        .map(Image::getUrl)
                        .collect(Collectors.toList()))
                .build();
    }
}
