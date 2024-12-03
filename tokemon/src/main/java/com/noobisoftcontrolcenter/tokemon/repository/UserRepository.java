package com.noobisoftcontrolcenter.tokemon.repository;

import com.noobisoftcontrolcenter.tokemon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
