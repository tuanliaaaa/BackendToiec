package com.toiec.toiec.repository;

import com.toiec.toiec.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository  extends JpaRepository<UserRole,Integer> {

}
