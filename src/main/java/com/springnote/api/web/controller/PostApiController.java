package com.springnote.api.web.controller;

import com.springnote.api.dto.post.common.PostPageResponseDto;
import com.springnote.api.dto.post.common.PostResponseDto;
import com.springnote.api.service.PostService;
import com.springnote.api.utils.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostApiController {

    private final PostService postService;
    private final UserContext userContext;

}
