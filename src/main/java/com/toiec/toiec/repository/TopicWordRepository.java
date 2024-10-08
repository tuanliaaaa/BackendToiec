package com.toiec.toiec.repository;

import com.toiec.toiec.entity.TopicWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicWordRepository extends JpaRepository<TopicWord,Integer> {
}
