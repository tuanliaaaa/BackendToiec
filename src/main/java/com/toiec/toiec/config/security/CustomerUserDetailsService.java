package com.toiec.toiec.config.security;

import com.toiec.toiec.entity.Role;
import org.springframework.security.core.userdetails.User;
import com.toiec.toiec.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername (String username)  {
        com.toiec.toiec.entity.User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username "  + " not found"));
        List<Role> userRoles = userRepository.findRolesByUsername(user.getUsername());
        // Tạo danh sách quyền từ role của Employee
        List<GrantedAuthority> authorities = userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority( userRole.getRoleName()))
                .collect(Collectors.toList());

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}