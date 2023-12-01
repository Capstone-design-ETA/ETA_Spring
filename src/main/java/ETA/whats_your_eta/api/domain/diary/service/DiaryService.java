package ETA.whats_your_eta.api.domain.diary.service;

import ETA.whats_your_eta.api.domain.diary.Diary;
import ETA.whats_your_eta.api.domain.diary.dto.DiaryRequestDto;
import ETA.whats_your_eta.api.domain.diary.dto.DiaryResponseDto;
import ETA.whats_your_eta.api.domain.diary.repository.DiaryRepository;
import ETA.whats_your_eta.api.domain.image.Image;
import ETA.whats_your_eta.api.domain.image.repository.ImageRepository;
import ETA.whats_your_eta.api.domain.image.service.S3Service;
import ETA.whats_your_eta.api.domain.user.User;
import ETA.whats_your_eta.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final ImageRepository imageRepository;
    private final UserService userService;
    private final S3Service s3Service;


    // 현재 유저가 쓴 일기 목록 조회
    @Transactional(readOnly = true)
    public List<DiaryResponseDto.Info> getAllDiaries() {
        User user = userService.getCurrentUser();

        List<Diary> diaries = diaryRepository.findAllByUserId(user.getId());
        return diaries.stream()
                .map(DiaryResponseDto.Info::of)
                .collect(Collectors.toList());
    }

    // 현재 유저 일기 작성
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

    // 현재 유저 일기 수정
    @Transactional
    public void updateDiary(Long diaryId, DiaryRequestDto req, List<MultipartFile> multipartFiles) {
        User user = userService.getCurrentUser();

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기가 없습니다. id=" + diaryId));

        if (!(diary.getUser().getId().equals(user.getId()))) {
            throw new IllegalArgumentException("다른 사용자의 일기는 수정할 수 없습니다.");
        }

        // 기존의 이미지를 S3에서 삭제하고 DB에서 삭제
        for (Image image : diary.getImages()) {
            s3Service.delete(image.getUrl());
        }
        imageRepository.deleteAllByDiaryIdInQuery(diaryId);
        imageRepository.flush();

        diary.setLocation(req.getLocation());
        diary.setDate(req.getDate());
        diary.setContent(req.getContent());

        // 새로운 이미지들 s3와 DB에 추가
        List<String> imgPaths = s3Service.upload(multipartFiles);

        for (String imgUrl: imgPaths) {
            Image image = Image.builder()
                    .url(imgUrl)
                    .diary(diary)
                    .build();
            imageRepository.save(image);
        }
    }
}
