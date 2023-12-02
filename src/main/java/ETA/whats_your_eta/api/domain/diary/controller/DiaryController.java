package ETA.whats_your_eta.api.domain.diary.controller;

import ETA.whats_your_eta.api.domain.diary.Diary;
import ETA.whats_your_eta.api.domain.diary.dto.DiaryRequestDto;
import ETA.whats_your_eta.api.domain.diary.dto.DiaryResponseDto;
import ETA.whats_your_eta.api.domain.diary.service.DiaryService;
import ETA.whats_your_eta.api.domain.image.dto.ImageResponseDto;
import ETA.whats_your_eta.api.domain.image.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;
    private final S3Service s3Service;

    @GetMapping("/list")
    @ResponseStatus(value = HttpStatus.OK)
    public List<DiaryResponseDto.Info> getAllDiaries() {
        return diaryService.getAllDiaries();
    }

    @GetMapping("/{location}/list")
    @ResponseStatus(value = HttpStatus.OK)
    public List<DiaryResponseDto.Info> getDiariesByLocation(@PathVariable String location) {
        return diaryService.getDiariesByLocation(location);
    }

    @GetMapping("/{location}/latest-image")
    @ResponseStatus(value = HttpStatus.OK)
    public ImageResponseDto getLatestDiaryFirstImageByLocation(@PathVariable String location) {
        return diaryService.getLatestDiaryFirstImageByLocation(location);
    }

    @PostMapping("/upload")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Void> postDiary(@RequestPart("diaryContent") DiaryRequestDto postRequestDto,
                                           @RequestPart("imgUrl") List<MultipartFile> multipartFiles) {
        List<String> imgPaths = s3Service.upload(multipartFiles);
        diaryService.uploadDiary(postRequestDto, imgPaths);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{diaryId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateDiary(@PathVariable Long diaryId,
                                             @RequestPart("diaryContent") DiaryRequestDto updateRequestDto,
                                             @RequestPart("imgUrl") List<MultipartFile> multipartFiles) {
        diaryService.updateDiary(diaryId, updateRequestDto, multipartFiles);
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.noContent().build();
    }
}
