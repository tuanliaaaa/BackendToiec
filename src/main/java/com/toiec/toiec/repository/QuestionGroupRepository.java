package com.toiec.toiec.repository;

import com.toiec.toiec.entity.QuestionGroup;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface QuestionGroupRepository extends JpaRepository<QuestionGroup,Integer> {

    @Query(nativeQuery = true, value = """
        SELECT 
            q.id_question as questionId, 
            a.id AS answer_id, 
            a.correct as correct
        FROM question_group qg
        JOIN question q ON qg.id_question_group = q.id_question_group
        JOIN answer a ON a.question_id = q.id_question
        JOIN (
            SELECT id_question_group
            FROM question_group
            WHERE type = :type
            LIMIT :limit OFFSET :offset
        ) AS limited_qg ON qg.id_question_group = limited_qg.id_question_group
        ORDER BY qg.id_question_group, questionId
    """)
    List<Map<String, Object>> findQuestionGroups(@Param("type") String type,
                                              @Param("limit") int limit,
                                              @Param("offset") int offset);


    @Query(value = """
            SELECT
                qg.id_question_group AS idQuestionGroup,
                qg.header_question_group AS questionGroup,
                (
                    SELECT
                        JSON_ARRAYAGG(
                            JSON_OBJECT(
                                'idQuestion', childQ.id_question,
                                'question', childQ.value,
                                'answerList', (
                                    SELECT
                                        JSON_ARRAYAGG(
                                            JSON_OBJECT(
                                                'idAnswer', a.id,
                                                'answer', a.value,
                                                'isCorrect', CAST(a.correct AS UNSIGNED)
                                            )
                                        )
                                    FROM answer a
                                    WHERE a.question_id = childQ.id_question
                                )
                            )
                        )
                    FROM question childQ
                    WHERE childQ.id_question_group = qg.id_question_group
                ) AS childQuestions,
                (
                    SELECT
                        JSON_ARRAYAGG(
                            JSON_OBJECT(
                                'idResource', r.id_resource,
                                'resourceType', r.resoure_type,
                                'resourceContent', r.content_resource
                            )
                        )
                    FROM resource_question_group rq
                    JOIN resource r ON r.id_resource = rq.id_resource
                    WHERE rq.id_question_group = qg.id_question_group
                ) AS resources
            FROM question_group qg
            WHERE qg.type = :type
            LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findParentQuestionGroupsWithChildQuestionsAndSharedResources(@Param("type") String type, @Param("limit") int limit);

    @Query(value = """
        SELECT
            qg.id_question_group AS idQuestionGroup,
            qg.header_question_group AS questionGroup,
            (
                SELECT
                    JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'idQuestion', childQ.id_question,
                            'question', childQ.value,
                            'answerList', (
                                SELECT
                                    JSON_ARRAYAGG(
                                        JSON_OBJECT(
                                            'idAnswer', a.id,
                                            'answer', a.value,
                                            'isCorrect', CAST(a.correct AS UNSIGNED)
                                        )
                                    )
                                FROM answer a
                                WHERE a.question_id = childQ.id_question
                            )
                        )
                    )
                FROM question childQ
                WHERE childQ.id_question_group = qg.id_question_group
            ) AS childQuestions,
            (
                SELECT
                    JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'idResource', r.id_resource,
                            'resourceType', r.resoure_type,
                            'resourceContent', r.content_resource
                        )
                    )
                FROM resource_question_group rq
                JOIN resource r ON r.id_resource = rq.id_resource
                WHERE rq.id_question_group = qg.id_question_group
            ) AS resources
        FROM question_group qg
        LEFT JOIN lesson_detail ld ON ld.id_lesson_detail = qg.idlesson_detail 
        WHERE ld.id_lesson_detail =:id
        
    """, nativeQuery = true)
    List<Object[]> findParentQuestionGroupsWithChildQuestionsAndResourcesAndLessonDetails(
            @Param("id") Integer id
    );



}
