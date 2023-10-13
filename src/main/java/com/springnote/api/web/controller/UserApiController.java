package com.springnote.api.web.controller;

import com.springnote.api.service.PostService;
import com.springnote.api.utils.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserApiController {

    @PostMapping("/register")
    public void registerUser() {

    }

    @GetMapping("/status")
    public void getUserStatus() {

    }

    @DeleteMapping("/withdraw")
    public void withdrawUser() {

    }


}
