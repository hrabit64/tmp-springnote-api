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
@RequestMapping("/api/v1/image")
public class ImageApiController {

    @GetMapping("/{imageId}")
    public void getImageById() {

    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @PostMapping("")
    public void createImage() {

    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @DeleteMapping("/{imageId}")
    public void deleteImage() {

    }

}
