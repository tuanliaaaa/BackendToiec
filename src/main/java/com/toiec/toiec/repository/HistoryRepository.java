package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Histories;
import com.toiec.toiec.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<Histories,Integer> {
    List<Histories> findByUser_UsernameAndTypeOrderByDateDesc(String username, String type, Pageable pageable);
}
