package com.toiec.toiec.repository;

import com.toiec.toiec.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository  extends JpaRepository<RefreshToken,Integer> {
}
