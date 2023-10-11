package com.springnote.api.domain.series;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesCustomRepository extends JpaRepository<Series, Long> {
}