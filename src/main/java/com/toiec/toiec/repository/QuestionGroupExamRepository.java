package com.toiec.toiec.repository;

import com.toiec.toiec.entity.QuestionGroupExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface QuestionGroupExamRepository extends JpaRepository<QuestionGroupExam, Integer> {
//    List<QuestionGroupExam> findByExam_IdAndOrderOfQuestionGroup(Integer examId, Integer orderOfQuestionGroup);
    @Query("SELECT qge FROM QuestionGroupExam qge WHERE qge.exam.idExam = :examId AND qge.questionGroup.idQuestionGroup = :questionGroupId")
    List<QuestionGroupExam> findByExamIdAndQuestionGroupId(Integer examId, Integer questionGroupId);
    @Query("SELECT qge FROM QuestionGroupExam qge " +
            "JOIN qge.questionGroup qg " +
            "WHERE qge.exam.idExam = :examId AND qg.type = :type")
    List<QuestionGroupExam> findByExamIdAndQuestionGroupType(@Param("examId") Integer examId,
                                                             @Param("type") String type);

}
