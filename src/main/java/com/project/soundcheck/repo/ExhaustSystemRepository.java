package com.project.soundcheck.repo;

import com.project.soundcheck.model.ExhaustSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExhaustSystemRepository extends JpaRepository<ExhaustSystem, Long> {
    List<ExhaustSystem> findByType(String type);
}
