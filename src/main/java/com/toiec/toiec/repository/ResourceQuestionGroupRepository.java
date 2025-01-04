package com.toiec.toiec.repository;

import com.toiec.toiec.entity.ResourceQuestionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceQuestionGroupRepository extends JpaRepository<ResourceQuestionGroup,Integer> {
}
