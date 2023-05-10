package com.serwertetowy.repos;

import com.serwertetowy.entities.SeriesTags;
import com.serwertetowy.entities.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesTagsRepository extends JpaRepository<SeriesTags, Integer> {
}
