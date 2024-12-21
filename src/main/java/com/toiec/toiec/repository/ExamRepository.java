package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam,Integer> {

        @Query(value = """
            SELECT 
                e.id_exam AS idExam,
                e.exam_name AS examName,
                JSON_OBJECT(
                    'part', qg.type,
                    'questionGroups', JSON_ARRAYAGG(
                        JSON_OBJECT(
                            'id', qg.id_question_group,
                            'name', qg.header_question_group,
                            'questionList', (
                                SELECT JSON_ARRAYAGG(
                                        JSON_OBJECT(
                                            'idQuestion', childQ.id_question,
                                            'question', childQ.value,
                                            'answerList', (
                                                SELECT JSON_ARRAYAGG(
                                                        JSON_OBJECT(
                                                            'idAnswer', a.id,
                                                            'answer', a.value
                                                        )
                                                    )
                                                FROM answer a
                                                WHERE a.question_id = childQ.id_question
                                            )
                                        )
                                    )
                                FROM question childQ
                                WHERE childQ.id_question_group = qg.id_question_group
                            ),
                            'resourceList', (
                                SELECT JSON_ARRAYAGG(
                                        JSON_OBJECT(
                                            'idResource', r.id_resource,
                                            'resourceType', r.resoure_type,
                                            'resourceContent', r.content_resource
                                        )
                                    )
                                FROM resource_question_group rq
                                JOIN resource r ON r.id_resource = rq.id_resource
                                WHERE rq.id_question_group = qg.id_question_group
                            )
                        )
                    ) 
                ) AS examDetails
            FROM exam e
            JOIN question_group_exam qge ON qge.id_exam = e.id_exam
            JOIN question_group qg ON qg.id_question_group = qge.id_question_group
            WHERE e.id_exam = :idExam
            GROUP BY e.id_exam, e.exam_name, qg.type, qge.order_of_question_group
            ORDER BY qg.type, qge.order_of_question_group;
        """, nativeQuery = true)
        List<Object[]> findExamDetails(@Param("idExam") Integer idExam);

        @Query(value = """
            SELECT 
                e.id_exam AS idExam,
                e.exam_name AS examName,
                CASE 
                    WHEN COUNT(qg.id_question_group) = 0 THEN JSON_ARRAY()
                    ELSE JSON_ARRAYAGG(
                        JSON_OBJECT(
                                'id', qg.id_question_group,
                                'name', qg.header_question_group,
                                'orderOfQuestionGroup',qge.order_of_question_group,
                                'questionList', (
                                    SELECT JSON_ARRAYAGG(
                                        JSON_OBJECT(
                                            'idQuestion', childQ.id_question,
                                            'question', childQ.value,
                                            'answerList', (
                                                SELECT 
                                                    CASE 
                                                        WHEN COUNT(a.id) = 0 THEN JSON_ARRAY()
                                                        ELSE
                                                            JSON_ARRAYAGG(
                                                                JSON_OBJECT(
                                                                    'idAnswer', a.id,
                                                                    'answer', a.value,
                                                                'isCorrect', CASE WHEN a.correct = '1' THEN TRUE ELSE FALSE END
                                                                )
                                                        )
                                                    END 
                                                FROM answer a
                                                WHERE a.question_id = childQ.id_question
                                            )
                                        )
                                    )
                                    FROM question childQ
                                    WHERE childQ.id_question_group = qg.id_question_group
                                ),
                                'resourceList', (
                                    SELECT 
                                        CASE 
                                            WHEN COUNT(r.id_resource) = 0 THEN JSON_ARRAY()
                                            ELSE
                                                JSON_ARRAYAGG(
                                                    JSON_OBJECT(
                                                        'idResource', r.id_resource,
                                                        'resourceType', r.resoure_type,
                                                        'resourceContent', r.content_resource
                                                    )
                                                )
                                        END
                                    FROM resource_question_group rq
                                    JOIN resource r ON r.id_resource = rq.id_resource
                                    WHERE rq.id_question_group = qg.id_question_group
                                )
                        )
                    )
                END AS examDetails
            FROM exam e
            JOIN question_group_exam qge ON qge.id_exam = e.id_exam
            JOIN question_group qg ON qg.id_question_group = qge.id_question_group AND qg.type = :part
            WHERE e.id_exam = :idExam
            GROUP BY e.id_exam, e.exam_name,qge.order_of_question_group
            ORDER BY qge.order_of_question_group;
        """, nativeQuery = true)

        List<Object[]> findExamDetailsByPart(@Param("idExam") Integer idExam,@Param("part") String  part);
}
