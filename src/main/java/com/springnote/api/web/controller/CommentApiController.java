//TODO : 댓글 필터링 구현 후, 댓글 기능 추가하기
package com.springnote.api.web.controller;

import com.springnote.api.dto.comment.common.CommentResponseDto;
import com.springnote.api.dto.comment.controller.CommentAddRequestControllerDto;
import com.springnote.api.dto.comment.controller.CommentUpdateRequestControllerDto;
import com.springnote.api.dto.comment.service.CommentAddRequestServiceDto;
import com.springnote.api.dto.comment.service.CommentReplyAddRequestServiceDto;
import com.springnote.api.dto.series.common.SeriesResponseDto;
import com.springnote.api.service.CommentService;
import com.springnote.api.service.PostService;
import com.springnote.api.utils.aop.auth.AuthLevel;
import com.springnote.api.utils.aop.auth.EnableAuth;
import com.springnote.api.utils.context.UserContext;
import com.springnote.api.utils.modelAssembler.CommentResponseDtoAssembler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comment")
public class CommentApiController {

    private final CommentService commentService;
    private final CommentResponseDtoAssembler commentResponseDtoAssembler;
    private final PagedResourcesAssembler<CommentResponseDto> pagedResourcesAssembler;
    private final UserContext userContext;

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @GetMapping("")
    public PagedModel<EntityModel<CommentResponseDto>> getComments(
            @RequestParam(value = "post",required = true) Long postId,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ){
        var comments = commentService.getCommentWithPost(pageable, postId);
        return pagedResourcesAssembler.toModel(comments, commentResponseDtoAssembler);
    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @GetMapping("/{commentId}")
    public EntityModel<CommentResponseDto> getCommentById(@PathVariable Long commentId) {
        var comment = commentService.getComment(commentId);
        return commentResponseDtoAssembler.toModel(comment);
    }

//    @EnableAuth(authLevel = AuthLevel.USER)
    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @PostMapping("/comment")
    public EntityModel<CommentResponseDto> createComment(
            @RequestBody @Validated CommentAddRequestControllerDto dto
    ) {
        var comment = commentService.createComment(dto.toServiceDto(userContext.getUid()));
        return commentResponseDtoAssembler.toModel(comment);
    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
//    @EnableAuth(authLevel = AuthLevel.USER)
    @PutMapping("/{commentId}")
    public EntityModel<CommentResponseDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Validated CommentUpdateRequestControllerDto dto
    ) {
        var comment = commentService.updateComment(dto.toServiceDto(userContext.getUid(), commentId));
        return commentResponseDtoAssembler.toModel(comment);
    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
//    @EnableAuth(authLevel = AuthLevel.USER)
    @DeleteMapping("/{commentId}")
    public EntityModel<CommentResponseDto> deleteComment(@PathVariable Long commentId) {
        var comment = commentService.deleteComment(commentId,userContext.getUid(),userContext.isAdmin());
        return commentResponseDtoAssembler.toModel(comment);
    }

}
