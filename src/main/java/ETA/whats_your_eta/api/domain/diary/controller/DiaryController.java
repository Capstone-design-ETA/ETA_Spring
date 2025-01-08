package ETA.whats_your_eta.api.domain.diary.controller;

import ETA.whats_your_eta.api.domain.diary.dto.DiaryRequestDto;
import ETA.whats_your_eta.api.domain.diary.dto.DiaryResponseDto;
import ETA.whats_your_eta.api.domain.diary.service.DiaryService;
import ETA.whats_your_eta.api.domain.image.dto.ImageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
@Tag(name = "Diary", description = "Diary management APIs")
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "Get all diaries", description = "Retrieve a list of all diaries.")
    @GetMapping("/list")
    public ResponseEntity<List<DiaryResponseDto.Info>> getAllDiaries() {
        List<DiaryResponseDto.Info> diaries = diaryService.getAllDiaries();
        if (diaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(diaries);
    }

    @Operation(summary = "Get diaries by location", description = "Retrieve a list of diaries for a specific location.")
    @GetMapping("/{location}/list")
    public ResponseEntity<List<DiaryResponseDto.Info>> getDiariesByLocation(@PathVariable String location) {
        List<DiaryResponseDto.Info> diaries = diaryService.getDiariesByLocation(location);
        if (diaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(diaries);
    }

    @Operation(summary = "Get the latest diary's first image by location", description = "Retrieve the first image of the latest diary for a specific location.")
    @GetMapping("/{location}/latest-image")
    public ResponseEntity<ImageResponseDto> getLatestDiaryFirstImageByLocation(@PathVariable String location) {
        return ResponseEntity.ok(diaryService.getLatestDiaryFirstImageByLocation(location));
    }

    @Operation(summary = "Upload a new diary", description = "Create a new diary with optional images.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> postDiary(@Valid @RequestPart("diaryContent") DiaryRequestDto postRequestDto,
                                           @RequestPart(value = "image", required = false) List<MultipartFile> images) {
        diaryService.uploadDiary(postRequestDto, images);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update an existing diary", description = "Modify the content and/or images of an existing diary.")
    @PutMapping("/{diaryId}")
    public ResponseEntity<Void> updateDiary(@PathVariable Long diaryId,
                                             @RequestPart("diaryContent") DiaryRequestDto updateRequestDto,
                                             @RequestPart(value = "image", required = false) List<MultipartFile> images) {
        diaryService.updateDiary(diaryId, updateRequestDto, images);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a diary", description = "Remove an existing diary by its ID.")
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.noContent().build();
    }
}
