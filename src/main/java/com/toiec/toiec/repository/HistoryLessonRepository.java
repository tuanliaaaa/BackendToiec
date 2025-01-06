package com.toiec.toiec.repository;

import com.toiec.toiec.entity.HistoryLesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryLessonRepository extends JpaRepository<HistoryLesson,Integer> {
    Page<HistoryLesson> findByUser_UsernameOrderByCreatedAtDesc(String username, Pageable pageable);
}
