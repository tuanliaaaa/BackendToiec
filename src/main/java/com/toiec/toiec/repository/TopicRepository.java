package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicRepository extends JpaRepository<Lesson,Integer> {

    @Query(value = "SELECT id_lesson, name_lesson FROM lesson WHERE type = 'vocabulary'", nativeQuery = true)
    List<Object[]> findAllVocabularyTopics();
}
