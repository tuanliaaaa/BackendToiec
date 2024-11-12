package com.toiec.toiec.repository;

import com.toiec.toiec.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History,Integer> {
    List<History> findByUser_UsernameAndTypeOrderByDoneAt(String username, String type, Pageable pageable);
}
