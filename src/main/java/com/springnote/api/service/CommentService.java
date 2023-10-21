package com.springnote.api.service;

import com.springnote.api.domain.jpa.comment.CommentRepository;
import com.springnote.api.domain.jpa.post.PostRepository;
import com.springnote.api.domain.jpa.user.UserRepository;
import com.springnote.api.dto.comment.common.CommentResponseDto;
import com.springnote.api.dto.comment.service.CommentAddRequestServiceDto;
import com.springnote.api.dto.comment.service.CommentReplyAddRequestServiceDto;
import com.springnote.api.dto.comment.service.CommentUpdateRequestServiceDto;
import com.springnote.api.utils.exception.auth.AuthErrorCode;
import com.springnote.api.utils.exception.auth.AuthException;
import com.springnote.api.utils.exception.service.ServiceErrorCode;
import com.springnote.api.utils.exception.service.ServiceException;
import com.springnote.api.utils.time.TimeUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TimeUtility timeUtility;

    @Transactional(readOnly = true)
    public CommentResponseDto getComment(Long id) {
        var target = commentRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 댓글이 존재하지 않습니다."));
        return new CommentResponseDto(target);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getCommentWithPost(Pageable pageable, Long postId) {
        var targetPost = postRepository.findById(postId)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));

        return commentRepository.findAllByIsReplyAndPost(pageable, false, targetPost)
                .map(CommentResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getReply(Pageable pageable, Long commentId) {
        var targetComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 댓글이 존재하지 않습니다."));
        return commentRepository.findAllByParentComment(pageable, targetComment)
                .map(CommentResponseDto::new);
    }

    @Transactional
    public CommentResponseDto createComment(CommentAddRequestServiceDto dto) {

        var writeUser = userRepository.findById(dto.getWriter())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 유저가 존재하지 않습니다."));
        var targetPost = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));

        var comment = dto.toEntity(writeUser, targetPost, timeUtility.nowDateTime());
        var savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment);

    }

    @Transactional
    public CommentResponseDto updateComment(CommentUpdateRequestServiceDto dto){
        var targetComment = commentRepository.findById(dto.getId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 댓글이 존재하지 않습니다."));

        if(!targetComment.getUser().getId().equals(dto.getWriter())){
            throw new AuthException(AuthErrorCode.AUTH_FAIL, "해당 댓글의 작성자가 아닙니다.");
        }

        targetComment.update(dto.toEntity(timeUtility.nowDateTime()));
        return new CommentResponseDto(targetComment);
    }

    @Transactional
    public CommentResponseDto deleteComment(Long commentId,String writer, boolean isAdmin) {
        var targetComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 댓글이 존재하지 않습니다."));

        if(!isAdmin && !targetComment.getUser().getId().equals(writer)){
            throw new AuthException(AuthErrorCode.AUTH_FAIL, "해당 댓글의 작성자가 아닙니다.");
        }

        if(targetComment.getIsDeleted()){
            throw new ServiceException(ServiceErrorCode.NOT_FOUND, "이미 삭제된 댓글입니다.");
        }
        targetComment.setDelete(timeUtility.nowDateTime());
        return new CommentResponseDto(targetComment);
    }

    @Transactional
    public CommentResponseDto createReply(CommentReplyAddRequestServiceDto dto){
        var parentComment = commentRepository.findById(dto.getParentCommentId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 댓글이 존재하지 않습니다."));

        var writeUser = userRepository.findById(dto.getWriter())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 유저가 존재하지 않습니다."));


        var comment = dto.toEntity(writeUser, parentComment.getPost(),parentComment, timeUtility.nowDateTime());

        var savedComment = commentRepository.save(comment);

        parentComment.addReply();
        commentRepository.save(parentComment);

        return new CommentResponseDto(savedComment);
    }
}
