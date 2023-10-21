package com.springnote.api.domain.jpa.post;

import com.springnote.api.domain.jpa.series.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsByTitle(String title);
    Page<Post> findAllBySeries(Pageable pageable, Series series);
    List<Post> findAllByIdIn(List<Long> ids);

}
