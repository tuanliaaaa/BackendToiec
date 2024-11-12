package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam,Integer> {
}
