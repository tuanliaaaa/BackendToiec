package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Integer> {
    @Query(value = "SELECT q.id_question, q.value AS questionValue, " +
            "(SELECT JSON_ARRAYAGG(JSON_OBJECT('idAnswer', a.id, 'answer', a.value, 'isCorrect', CAST(a.correct AS UNSIGNED))) " +
            " FROM answer a WHERE a.question_id = q.id_question) AS answers, " +
            "(SELECT JSON_ARRAYAGG(JSON_OBJECT('idResource', r.id_resource, 'resourceType', r.resoure_type, 'resourceContent', r.content_resource)) " +
            " FROM resource_question RQ JOIN resource r ON r.id_resource = RQ.id_resource WHERE RQ.id_question = q.id_question) AS resources " +
            "FROM (SELECT id_question, value FROM question WHERE type = :type LIMIT :limit) q",
            nativeQuery = true)
    List<Object[]> findQuestionsWithAnswersByType(@Param("type") String type, @Param("limit") int limit);



}
