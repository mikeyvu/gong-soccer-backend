package com.mikey.gongsoccer.repository;

import com.mikey.gongsoccer.entity.team.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    Optional<TeamEntity> findTeamEntitiesById(Long id);
}
