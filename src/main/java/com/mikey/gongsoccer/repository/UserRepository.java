package com.mikey.gongsoccer.repository;

import com.mikey.gongsoccer.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntitiesByEmail(String email);
}
