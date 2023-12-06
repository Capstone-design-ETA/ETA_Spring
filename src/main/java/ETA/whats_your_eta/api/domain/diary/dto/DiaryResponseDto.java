package ETA.whats_your_eta.api.domain.diary.dto;

import ETA.whats_your_eta.api.domain.diary.Diary;
import ETA.whats_your_eta.api.domain.image.Image;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class DiaryResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    static public class Info {
        private Long id;
        private String location;
        private Double latitude;
        private Double longitude;
        private String date;
        private String userName;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private String content;
        private List<String> imageUrls;

        public static Info of(Diary diary) {
            return Info.builder()
                    .id(diary.getId())
                    .location(diary.getLocation())
                    .latitude(diary.getLatitude())
                    .longitude(diary.getLongitude())
                    .date(diary.getDate())
                    .userName(diary.getUser().getName())
                    .createdAt(diary.getCreatedAt())
                    .modifiedAt(diary.getModifiedAt())
                    .content(diary.getContent())
                    .imageUrls(diary.getImages().stream()
                            .map(Image::getUrl)
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
