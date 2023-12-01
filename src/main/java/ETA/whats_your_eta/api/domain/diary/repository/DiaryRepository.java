package ETA.whats_your_eta.api.domain.diary.repository;

import ETA.whats_your_eta.api.domain.diary.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findAllByUserId(Long userId);
}