package com.springnote.api.domain.elasticsearch.postIndex;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostIndexRepository extends ElasticsearchRepository<PostIndex, Long> {
}
