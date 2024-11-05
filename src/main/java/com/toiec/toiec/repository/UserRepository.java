package com.toiec.toiec.repository;

import com.toiec.toiec.entity.Role;
import com.toiec.toiec.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
    @Query("SELECT ur.role FROM UserRole ur WHERE ur.user.username = :username")
    List<Role> findRolesByUsername(@Param("username") String username);

    @Query(value = "SELECT a.id_User,a.username,a.status,r.id_Role, r.role_name " +
            "FROM user a " +
            "JOIN user_role ar ON a.id_User = ar.id_user " +
            "JOIN role r ON ar.id_role = r.id_Role " +
            "WHERE a.username = :username ", nativeQuery = true)
    List<Object[]> findInforByUsernameWithRoles(@Param("username") String username);
}
