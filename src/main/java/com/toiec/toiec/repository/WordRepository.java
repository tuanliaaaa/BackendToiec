package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Lesson;
import com.toiec.toiec.entity.LessonDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WordRepository  extends JpaRepository<LessonDetail,Integer> {
    @Query("SELECT w FROM LessonDetail w WHERE w.lesson.idLesson= :topicId ")
    List<LessonDetail> findByTopicWordId(@Param("topicId") int topicId);
}
