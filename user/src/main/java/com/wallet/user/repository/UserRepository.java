package com.wallet.user.repository;

import com.wallet.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User getUserById(Long id);

    List<User> findAll();
}
