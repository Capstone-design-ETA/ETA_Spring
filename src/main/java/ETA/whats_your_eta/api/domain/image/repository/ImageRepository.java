package ETA.whats_your_eta.api.domain.image.repository;

import ETA.whats_your_eta.api.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ImageRepository extends JpaRepository<Image, Long> {
    @Modifying
    @Query("delete from Image i where i.diary.id = :diaryId")
    void deleteAllByDiaryIdInQuery(@Param("diaryId") Long diaryId);
}
