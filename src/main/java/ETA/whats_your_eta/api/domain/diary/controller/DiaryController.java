package ETA.whats_your_eta.api.domain.diary.controller;

import ETA.whats_your_eta.api.domain.diary.Diary;
import ETA.whats_your_eta.api.domain.diary.dto.DiaryRequestDto;
import ETA.whats_your_eta.api.domain.diary.dto.DiaryResponseDto;
import ETA.whats_your_eta.api.domain.diary.service.DiaryService;
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

//    @GetMapping("/list")
//    public Map<String, List<DiaryResponseDto>> getAllDiary() {
//        return
//    }

    @PostMapping("/upload")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Void> postDiary(@RequestPart("diaryContent") DiaryRequestDto postRequestDto,
                                           @RequestPart("imgUrl") List<MultipartFile> multipartFiles) {
        List<String> imgPaths = s3Service.upload(multipartFiles);
        System.out.println("IMG 경로들 " + imgPaths);
        diaryService.uploadDiary(postRequestDto, imgPaths);
        return ResponseEntity.ok().build();
    }
}
