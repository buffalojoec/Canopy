package com.canopy.canopyspring.runs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunRepository extends JpaRepository<Run, Long> {
}
