package com.springnote.api.web.controller;

import com.springnote.api.service.PostService;
import com.springnote.api.utils.aop.auth.AuthLevel;
import com.springnote.api.utils.aop.auth.EnableAuth;
import com.springnote.api.utils.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comment")
public class CommentApiController {

    @GetMapping("/comment")
    public void getComments() {

    }

    @GetMapping("/comment/{commentId}")
    public void getCommentById() {

    }

    @EnableAuth(authLevel = AuthLevel.USER)
    @PostMapping("/comment")
    public void createComment() {

    }

    @EnableAuth(authLevel = AuthLevel.USER)
    @PutMapping("/{commentId}")
    public void updateComment() {

    }

    @EnableAuth(authLevel = AuthLevel.USER)
    @DeleteMapping("/{commentId}")
    public void deleteComment() {

    }

}
