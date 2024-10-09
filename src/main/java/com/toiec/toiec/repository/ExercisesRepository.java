package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExercisesRepository  extends JpaRepository<Exercise,Integer> {
}
