package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
            JOIN question_group qg ON qg.id_exam = e.id_exam
            WHERE e.id_exam = :idExam
            GROUP BY e.id_exam, e.exam_name, qg.type
            ORDER BY qg.type;
        """, nativeQuery = true)
        List<Object[]> findExamDetails(@Param("idExam") Integer idExam);

}
