package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Integer> {
    @Query("SELECT c FROM Chat c " +
            "LEFT JOIN c.sender s " +
            "LEFT JOIN c.received r " +
            "WHERE s.username = :username OR r.username = :username " +
            "ORDER BY c.date DESC" )
    List<Chat> findChatsByUsername(@Param("username") String username, Pageable pageable);
}
