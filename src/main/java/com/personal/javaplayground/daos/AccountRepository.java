package com.personal.javaplayground.daos;

import com.personal.javaplayground.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
