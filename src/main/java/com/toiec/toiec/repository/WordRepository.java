package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WordRepository  extends JpaRepository<Word,Integer> {
    @Query("SELECT w FROM Word w WHERE w.topicWord.idTopicWord = :topicId")
    List<Word> findByTopicWordId(@Param("topicId") int topicId);
}
