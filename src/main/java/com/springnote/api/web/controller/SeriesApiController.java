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
@RequestMapping("/api/v1/series")
public class SeriesApiController {


    @GetMapping("")
    public void getSeries() {

    }

    @GetMapping("/search")
    public void getSeriesWithSearch() {

    }

    @GetMapping("/{seriesId}")
    public void getSeriesById() {

    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @PostMapping("")
    public void createSeries() {

    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @PutMapping("/{seriesId}")
    public void updateSeries() {

    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @DeleteMapping("/{seriesId}")
    public void deleteSeries() {

    }
}
