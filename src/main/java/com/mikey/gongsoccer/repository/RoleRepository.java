package com.mikey.gongsoccer.repository;

import com.mikey.gongsoccer.entity.user.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findRoleEntitiesByName(String name);
}
