package com.toiec.toiec.repository;

import com.toiec.toiec.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History,Integer> {
    List<History> findByHistoryLesson_IdHistoryLessonAndLessonDetail_IdLessonDetail(Integer idHistoryLesson, Integer idLessonDetail);



    List<History> findByUser_UsernameAndTypeContainingOrderByDoneAtDesc(String username, String type, Pageable pageable);
    List<History> findByUser_UsernameAndTypeOrderByDoneAtDesc(String username, String type, Pageable pageable);

    @Query(value = "SELECT h.id_history, h.type, h.amount_question_group, " +
            "h.status, h.created_at, h.done_at, h.score, u.username, " +
            "JSON_ARRAYAGG(JSON_OBJECT(" +
            "'idHistoryDetail', hd.id_history_detail, " +
            "'score', hd.score, " +
            "'idLessonDetail', hd.id_lesson_detail " +
            ")) AS history_details_json " +
            "FROM history h " +
            "JOIN history_detail hd ON h.id_history = hd.id_history " +
            "JOIN user u ON h.id_user = u.id_user " +
            "WHERE u.username = :username  and h.type='vocabulary' " +
            "GROUP BY h.id_history, h.type, h.amount_question_group, h.status, h.created_at, h.done_at, h.score, u.username " +
            "ORDER BY h.created_at DESC " +
            "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Object[]> findHistoriesWithDetailsByUsername(@Param("username") String username, @Param("limit") int limit, @Param("offset") int offset);
    @Query(value = """
            SELECT 
                hl.id_history_lesson AS idHistoryLesson, 
                hl.type AS type, 
                AVG(h.score) AS avgScore,
                hl.created_at, 
                u.username,
                l.id_lesson AS lessonId, 
                l.name_lesson AS lessonName, 
                JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'hdIdHistory', h.id_history,
                        'idLessonDetail', h.id_Lesson_Detail,
                        'score', h.score
                    )
                ) AS histories
            FROM 
                history_lesson hl
            JOIN 
                user u ON hl.id_user = u.id_user
            JOIN 
                lesson l ON hl.id_lesson = l.id_lesson
            JOIN 
                history h ON h.id_history_lesson = hl.id_history_lesson
            WHERE 
                u.username = :username And (:type IS NULL OR hl.type = :type ) And (:idLesson IS NULL OR hl.id_lesson = :idLesson )
            GROUP BY 
                hl.id_history_lesson, hl.type, l.id_lesson, l.name_lesson
            ORDER BY 
                hl.created_at DESC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<Object[]> findHistoriesVocabularyWithDetailsByUsername(@Param("username") String username,
                                                   @Param("limit") int limit,
                                                   @Param("offset") int offset,
                                                    @Param("type") String type,
                                                                @Param("idLesson") Integer idLesson);
    @Query(value = """
            SELECT 
                hl.id_history_lesson AS idHistoryLesson, 
                hl.type AS type,
                hl.created_at, 
                u.username,
                JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'hdIdHistory', h.id_history,
                        'idLessonDetail', h.id_Lesson_Detail,
                        'score', h.score
                    )
                ) AS histories
            FROM 
                history_lesson hl
            JOIN 
                user u ON hl.id_user = u.id_user
            JOIN 
                history h ON h.id_history_lesson = hl.id_history_lesson
            WHERE 
                u.username = :username And (:type IS NULL OR hl.type = :type ) 
            GROUP BY 
                hl.id_history_lesson, hl.type
            ORDER BY 
                hl.created_at DESC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<Object[]> findHistoriesLearningPathWithDetailsByUsername(@Param("username") String username,
                                                                @Param("limit") int limit,
                                                                @Param("offset") int offset,
                                                                @Param("type") String type);


    @Query(value = """
        SELECT 
            l.id_Lesson,
            l.name_lesson AS lessonname,
            ROUND(AVG(h.score), 2) AS point,
            JSON_ARRAYAGG(
                JSON_OBJECT(
                    'idLessonDetail', ld.id_Lesson_Detail,
                    'nameLessonDetail', ld.name_lesson,
                    'score', IFNULL(h.score, NULL)
                )
            ) AS lessonDetails
        FROM 
            Lesson l
        LEFT JOIN 
            lesson_detail ld ON l.id_Lesson = ld.id_Lesson
        LEFT JOIN 
            History h ON ld.id_Lesson_Detail = h.id_Lesson_Detail
        GROUP BY 
            l.id_Lesson, l.name_lesson
        """, nativeQuery = true)
    List<Object[]> getLessonsWithDetails();

    @Query(value = """
        SELECT 
            h.id_history AS idHistory, 
            h.amount_question_group AS amountQuestionGroup, 
            h.created_at AS createdAt, 
            h.done_at AS doneAt, 
            h.type AS type, 
            h.score AS score, 
            hd.id_question_group AS idQuestionGroup,
            qg.header_question_group AS questionGroupName,
            q.id_question AS idQuestion,
            q.value AS question,
            a.id AS idAnswer,
            a.value AS answer,
            a.correct  AS isCorrect,
            r.id_resource AS idResource,
            r.resoure_type AS resourceType,
            r.content_resource AS resourceContent,
            ua.id_user_answer AS idUserAnswer,
            ua.id_answer AS idAnswerSelected,
            hd.is_correct
        FROM history h
        JOIN history_detail hd ON h.id_history = hd.id_history 
        JOIN user u ON h.id_user = u.id_user
        JOIN question_group qg ON hd.id_question_group = qg.id_question_group
        LEFT JOIN question q ON q.id_question_group = qg.id_question_group
        LEFT JOIN answer a ON a.question_id = q.id_question
        LEFT JOIN resource_question_group rq ON rq.id_question_group = qg.id_question_group
        LEFT JOIN resource r ON r.id_resource = rq.id_resource
        LEFT JOIN user_answer ua ON ua.id_history_detail = hd.id_history_detail
        WHERE h.id_history = :idHistory
        """, nativeQuery = true)
    List<Object[]> findHistoryWithExamDetails(@Param("idHistory") Integer idHistory);

    @Query(value = """
    SELECT 
        h.id_history AS idHistory,
        h.amount_question_group AS amountQuestionGroup,
        h.created_at AS createdAt,
        h.done_at AS doneAt,
        h.type AS type,
        h.score AS score,
        ua.id_answer,
        hd.is_correct,
        hd.id_question,
        hd.id_question_group
    FROM history h
    JOIN history_detail hd ON h.id_history = hd.id_history
    JOIN user_answer ua ON hd.id_history_detail = ua.id_history_detail
    WHERE h.id_history = :idHistory
""", nativeQuery = true)
    List<Object[]> findHistoryOfLessonByIdHistoryAndUsername(@Param("idHistory") Integer idHistory);

}
