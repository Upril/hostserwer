package com.serwertetowy.repos;

import com.serwertetowy.entities.WatchFlags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchFlagRepository extends JpaRepository<WatchFlags, Integer> {
}
