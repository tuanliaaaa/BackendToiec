package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoadmapRepository extends JpaRepository<Lesson,Integer> {

    @Query(value = """
            SELECT l.id_lesson AS idLesson,
                   l.name_lesson AS name,
                   JSON_ARRAYAGG(
                       JSON_OBJECT(
                           'idDetail', d.id_lesson_detail,
                           'content', d.content,
                           'nameLesson',d.name_lesson,
                           'type', d.type
                       )
                   ) AS detailLessons
            FROM lesson l
            LEFT JOIN lesson_detail d ON l.id_lesson = d.id_lesson
            WHERE l.id_lesson = :id
            GROUP BY l.id_lesson
            """, nativeQuery = true)
    List<Object[]> findRoadmapWithLessonDetail(@Param("id") Integer id);
    @Query(value = """
            SELECT l.id_lesson AS idLesson,
                   l.name_lesson AS name,
                   JSON_ARRAYAGG(
                       JSON_OBJECT(
                           'idDetail', d.id_lesson_detail,
                           'content', d.content,
                           'nameLesson',d.name_lesson,
                           'type', d.type
                       )
                   ) AS detailLessons
            FROM lesson l
            LEFT JOIN lesson_detail d ON l.id_lesson = d.id_lesson
            WHERE l.type='roadmap'
            GROUP BY l.id_lesson
            """, nativeQuery = true)
    List<Object[]> findAllRoadmapWithLessonDetail();

}
