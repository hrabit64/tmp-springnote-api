package com.springnote.api.domain.jpa.series;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {
    boolean existsByTitle(String title);
    Optional<Series> findByTitle(String title);
}
