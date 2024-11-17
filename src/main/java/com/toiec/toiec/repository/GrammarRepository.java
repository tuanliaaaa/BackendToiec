package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Lesson;
import com.toiec.toiec.entity.LessonDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrammarRepository extends JpaRepository<LessonDetail,Integer> {
    Optional<LessonDetail> findById(int id);
}
