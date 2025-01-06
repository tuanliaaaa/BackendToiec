package com.toiec.toiec.repository;

import com.toiec.toiec.entity.LessonDetailQuestionGroup;
import com.toiec.toiec.entity.QuestionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonDetailQuestionGroupRepository extends JpaRepository<LessonDetailQuestionGroup, Integer> {
    @Query("SELECT ldqg.questionGroup FROM LessonDetailQuestionGroup ldqg WHERE ldqg.lessonDetail.idLessonDetail = :idLessonDetail")
    List<QuestionGroup> findQuestionGroupsByLessonDetailId(@Param("idLessonDetail") Integer idLessonDetail);
}
