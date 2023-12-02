package ETA.whats_your_eta.api.domain.image.dto;

import ETA.whats_your_eta.api.domain.image.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ImageResponseDto {
    private String url;

    public static ImageResponseDto of(Image image) {
        return ImageResponseDto.builder()
                .url(image.getUrl())
                .build();
    }
}
