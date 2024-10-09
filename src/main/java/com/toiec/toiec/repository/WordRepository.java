//package com.toiec.toiec.repository;
//
//import com.toiec.toiec.entity.Lesson;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//@Repository
//public interface WordRepository  extends JpaRepository<Lesson,Integer> {
//    @Query("SELECT w FROM Lesson w WHERE w.topicWord.idTopicWord = :topicId")
//    List<Lesson> findByTopicWordId(@Param("topicId") int topicId);
//}
