package com.springnote.api.web.controller;


import com.springnote.api.dto.post.common.PostResponseDto;
import com.springnote.api.dto.post.controller.*;
import com.springnote.api.service.PostService;
import com.springnote.api.utils.aop.auth.AuthLevel;
import com.springnote.api.utils.aop.auth.EnableAuth;
import com.springnote.api.utils.context.UserContext;
import com.springnote.api.utils.exception.controller.ControllerErrorCode;
import com.springnote.api.utils.exception.controller.ControllerException;
import com.springnote.api.utils.modelAssembler.PostResponseControllerDtoAssembler;
import com.springnote.api.utils.modelAssembler.PostResponseDtoAssembler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostApiController {

    private final PostService postService;
    private final PostResponseControllerDtoAssembler postResponseControllerDtoAssembler;
    private final PagedResourcesAssembler<PostResponseDto> pagedResourcesAssembler;
    private final PostResponseDtoAssembler postResponseDtoAssembler;

    //일반적인 다중조회, 본문 검색을 제외한 모든 경우
    @GetMapping("")
    public PagedModel<EntityModel<PostResponseDto>> getPosts(
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "null") Long series
    ) {
        Page<PostResponseDto> result;
        if (series != null) {
            result = postService.getAllPostWithSeries(pageable, series);

        } else {
            result = postService.getAllPost(pageable);
        }
        return pagedResourcesAssembler.toModel(result, postResponseDtoAssembler);
    }


    //sphinx 검색엔진을 이용한 검색, 제목 + 본문
    @GetMapping("/search")
    public PagedModel<EntityModel<PostResponseDto>> getPostsWithSearch(
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "") String content
    ) {
        isQueryParamValid(title, content);
        var result = postService.getAllPostWithIndex(pageable, title, content);

        return pagedResourcesAssembler.toModel(result, postResponseDtoAssembler);

    }

    @GetMapping("/recommend")
    public ResponseEntity<PostResponseListControllerDto> getRecommendedPosts() {
        var result = postService.getRecommendPost();
        var data = result.stream()
                .map(postResponseDtoAssembler::toModel)
                .toList();

        return ResponseEntity.ok(new PostResponseListControllerDto(data));
    }

    @GetMapping("/{postId}")
    public EntityModel<PostResponseControllerDto> getPostById(
            @PathVariable Long postId,
            @RequestParam(required = false, defaultValue = "true") Boolean render
    ) {
        var result = postService.getPostById(postId, render);
        return postResponseControllerDtoAssembler.toModel(result.toControllerDto());
    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @PostMapping("")
    public EntityModel<PostResponseControllerDto> createPost(
            @RequestBody @Validated PostAddRequestControllerDto dto
    ) {
        var result = postService.createPost(dto.toServiceDto());
        return postResponseControllerDtoAssembler.toModel(result.toControllerDto());
    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @PutMapping("/{postId}")
    public EntityModel<PostResponseControllerDto> updatePost(@PathVariable Long postId, @RequestBody @Validated PostUpdateRequestControllerDto dto) {
        var result = postService.updatePost(dto.toServiceDto(postId));
        return postResponseControllerDtoAssembler.toModel(result.toControllerDto());
    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @DeleteMapping("/{postId}")
    public EntityModel<PostResponseControllerDto> deletePost(@PathVariable Long postId) {
        var result = postService.deletePost(postId);
        return postResponseControllerDtoAssembler.toModel(result.toControllerDto());
    }

    private void isQueryParamValid(String title, String content) {
        if (title.equals("") && content.equals("")) {
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "검색 옵션이 비워져 있습니다.");
        }
        if (!title.equals("") && title.length() < 2) {
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "제목은 2글자 이상이어야 합니다.");
        }
        if (title.length() > 10) {
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "제목은 10글자 이하이어야 합니다.");
        }
        if (!content.equals("") && content.length() < 2) {
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "본문 검색어는 2글자 이상이어야 합니다.");
        }
        if (content.length() > 12) {
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "본문 검색어는 12자 이하이어야 합니다.");
        }
    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @PostMapping("/upload")
    public EntityModel<PostResponseControllerDto> uploadPost(
            @RequestPart String title,
            @RequestPart String seriesName,
            @RequestPart String thumbnail,
            @RequestPart MultipartFile markdownFile
    ) throws IOException {
        validateArg(title, seriesName, thumbnail);

        var dto = PostUploadRequestControllerDto.builder()
                .title(title)
                .seriesName(seriesName)
                .thumbnail(thumbnail)
                .build();
        var result = postService.uploadPost(dto.toServiceDto(new String(markdownFile.getBytes())));
        return postResponseControllerDtoAssembler.toModel(result.toControllerDto());
    }

    private void validateArg(String title, String seriesName, String thumbnail) {

        if (title.length() > 50 || title.length() == 0) {
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "제목은 50자 이하이어야 합니다.");
        }

        if (seriesName.length() > 50 || seriesName.length() == 0) {
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "시리즈 이름은 50자 이하이어야 합니다.");
        }

        if (thumbnail.length() > 260 || thumbnail.length() == 0) {
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "썸네일 주소는 260자 이하이어야 합니다.");
        }
    }

    private void validationFileType(MultipartFile markdownFile) {
        var fileName = markdownFile.getOriginalFilename();
        var extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        var allowedExtension = List.of("md");
        if (!allowedExtension.contains(extension)) {
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "파일 확장자는 md 만 가능합니다.");
        }
    }
}
