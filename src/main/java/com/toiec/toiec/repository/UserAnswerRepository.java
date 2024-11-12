package com.toiec.toiec.repository;

import com.toiec.toiec.entity.History;
import com.toiec.toiec.entity.UserAnswer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserAnswerRepository extends JpaRepository<UserAnswer,Integer> {
}
