package com.springnote.api.web.controller;

import com.springnote.api.dto.post.common.PostPageResponseDto;
import com.springnote.api.dto.post.common.PostResponseDto;
import com.springnote.api.service.PostService;
import com.springnote.api.utils.aop.auth.AuthLevel;
import com.springnote.api.utils.aop.auth.EnableAuth;
import com.springnote.api.utils.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostApiController {

    private final PostService postService;

    //일반적인 다중조회, 본문 검색을 제외한 모든 경우
    @GetMapping("")
    public void getPosts() {

    }


    //sphinx 검색엔진을 이용한 검색, 제목 + 본문
    @GetMapping("/search")
    public void getPostsWithSearch() {

    }

    @GetMapping("/recommend")
    public void getRecommendedPosts() {

    }

    @GetMapping("/{postId}")
    public void getPostById() {

    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @PostMapping("")
    public void createPost() {

    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @PutMapping("/{postId}")
    public void updatePost() {

    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @DeleteMapping("/{postId}")
    public void deletePost() {

    }
}
