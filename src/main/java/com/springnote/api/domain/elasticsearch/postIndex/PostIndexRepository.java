package com.springnote.api.domain.elasticsearch.postIndex;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostIndexRepository extends ElasticsearchRepository<PostIndex, Long> {

    Page<PostIndex> findAllByTitleLike(String title, Pageable pageable);
    Page<PostIndex> findAllByContentLike(String content, Pageable pageable);
    Page<PostIndex> findAllByTitleLikeOrContentLike(String title, String content, Pageable pageable);
}
