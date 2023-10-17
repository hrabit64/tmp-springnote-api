package com.springnote.api.domain.elasticsearch.seriesIndex;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesIndexRepository extends ElasticsearchRepository<SeriesIndex, Long> {

    Page<SeriesIndex> findAllByDescriptionLike(String description, Pageable pageable);


    Page<SeriesIndex> findAllByTitleLike(String title, Pageable pageable);


    Page<SeriesIndex> findAllByDescriptionLikeOrTitleLike(String description, String title, Pageable pageable);
}
