package com.KrishiG.repositories;

import com.KrishiG.enitites.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
