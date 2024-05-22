package com.personal.javaplayground.daos;

import com.personal.javaplayground.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}