package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Lesson;
import com.toiec.toiec.entity.LessonDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GrammarRepository extends JpaRepository<LessonDetail,Integer> {
    Optional<LessonDetail> findById(int id);
    Page<LessonDetail> findByNameLessonContainingIgnoreCaseAndType(String nameLesson,String type, Pageable pageable);
    List<LessonDetail>  findByLesson_IdLesson(Integer lessonId);
    Page<LessonDetail> findByType(String type, Pageable pageable);

}
