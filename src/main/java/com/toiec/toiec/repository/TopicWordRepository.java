package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicWordRepository extends JpaRepository<Lesson,Integer> {
}
