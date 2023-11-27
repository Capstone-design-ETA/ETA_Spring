package ETA.whats_your_eta.api.domain.diary.service;

import ETA.whats_your_eta.api.domain.diary.Diary;
import ETA.whats_your_eta.api.domain.diary.dto.DiaryRequestDto;
import ETA.whats_your_eta.api.domain.diary.repository.DiaryRepository;
import ETA.whats_your_eta.api.domain.image.Image;
import ETA.whats_your_eta.api.domain.image.repository.ImageRepository;
import ETA.whats_your_eta.api.domain.user.User;
import ETA.whats_your_eta.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final ImageRepository imageRepository;
    private final UserService userService;

    // 일기 작성
    @Transactional
    public void uploadDiary(DiaryRequestDto req, List<String> imgPaths) {
        User user = userService.getCurrentUser();

        Diary diary = req.toEntity();
        diary.setUser(user);

        Diary savedDiary = diaryRepository.save(diary);
        log.info(String.valueOf(savedDiary));

        for (String imgUrl : imgPaths) {
            Image image = Image.builder()
                    .url(imgUrl)
                    .diary(savedDiary)
                    .build();
            imageRepository.save(image);
        }
    }
}
