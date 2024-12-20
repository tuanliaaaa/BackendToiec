package com.toiec.toiec.repository;

import com.toiec.toiec.entity.QuestionGroupExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface QuestionGroupExamRepository extends JpaRepository<QuestionGroupExam, Integer> {
//    List<QuestionGroupExam> findByExam_IdAndOrderOfQuestionGroup(Integer examId, Integer orderOfQuestionGroup);
    @Query("SELECT qge FROM QuestionGroupExam qge WHERE qge.exam.idExam = :examId AND qge.questionGroup.idQuestionGroup = :questionGroupId")
    List<QuestionGroupExam> findByExamIdAndQuestionGroupId(Integer examId, Integer questionGroupId);

}
