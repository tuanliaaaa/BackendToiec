package com.toiec.toiec.repository;

import com.toiec.toiec.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History,Integer> {
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

}
