package com.springnote.api.service;

import com.springnote.api.domain.elasticsearch.postIndex.PostIndex;
import com.springnote.api.domain.elasticsearch.postIndex.PostIndexRepository;
import com.springnote.api.domain.jpa.post.Post;
import com.springnote.api.domain.jpa.post.PostRepository;
import com.springnote.api.domain.jpa.postContent.PostContent;
import com.springnote.api.domain.jpa.postContent.PostContentRepository;
import com.springnote.api.domain.jpa.postConvertContent.PostConvertContent;
import com.springnote.api.domain.jpa.postConvertContent.PostConvertContentRepository;
import com.springnote.api.domain.jpa.series.Series;
import com.springnote.api.domain.jpa.series.SeriesRepository;
import com.springnote.api.dto.post.common.PostResponseDto;
import com.springnote.api.dto.post.service.PostAddRequestServiceDto;
import com.springnote.api.dto.post.service.PostResponseServiceDto;
import com.springnote.api.dto.post.service.PostUpdateRequestServiceDto;
import com.springnote.api.dto.post.service.PostUploadRequestServiceDto;
import com.springnote.api.utils.exception.service.ServiceErrorCode;
import com.springnote.api.utils.exception.service.ServiceException;
import com.springnote.api.utils.markdown.MarkdownConvertor;
import com.springnote.api.utils.time.TimeUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostIndexRepository postIndexRepository;
    private final PostConvertContentRepository postConvertContentRepository;
    private final PostContentRepository postContentRepository;
    private final SeriesRepository seriesRepository;
    private final TimeUtility timeUtility;
    private final MarkdownConvertor markdownConvertor;
    private final ConfigService configService;

    @Transactional(readOnly = true)
    public PostResponseServiceDto getPostById(Long postId, boolean isRender) {

        var targetPost = postRepository.findById(postId)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "포스트를 찾을 수 없습니다."));

        String content;

        if (isRender) {
            var postConvertContent = postConvertContentRepository.findByPost(targetPost)
                    .orElseThrow(() -> new ServiceException(ServiceErrorCode.UNKNOWN_ERROR, "변환된 포스트 내용을 찾을 수 없습니다."));
            content = postConvertContent.getText();
        } else {
            var postContent = postContentRepository.findByPost(targetPost)
                    .orElseThrow(() -> new ServiceException(ServiceErrorCode.UNKNOWN_ERROR, "포스트 내용을 찾을 수 없습니다."));
            content = postContent.getText();
        }

        return new PostResponseServiceDto(targetPost, content, isRender);

    }

    @Transactional
    public PostResponseServiceDto createPost(PostAddRequestServiceDto dto) {
        //create new post
        checkExistTitle(dto.getTitle());
        var targetSeries = seriesRepository.findById(dto.getSeriesId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "시리즈를 찾을 수 없습니다."));

        var post = dto.toEntity(targetSeries, timeUtility.nowDateTime());
        var newPost = postRepository.saveAndFlush(post);

        //create post content
        var renderContent = markdownConvertor.convertToHtml(dto.getContent());
        var newPostContent = postContentRepository.save(PostContent
                .builder()
                .post(newPost)
                .text(dto.getContent())
                .build()
        );
        var newPostConvertContent = postConvertContentRepository.save(PostConvertContent
                .builder()
                .post(newPost)
                .text(renderContent)
                .build()
        );

        //create post index
        var plainText = markdownConvertor.convertToPlainText(newPostConvertContent.getText(), true);
        var newPostIndex = newPost.toIndex(plainText);
        postIndexRepository.save(newPostIndex);

        return new PostResponseServiceDto(newPost, newPostConvertContent.getText(), true);
    }

    @Transactional
    public PostResponseServiceDto uploadPost(PostUploadRequestServiceDto dto) {

        var targetSeries = seriesRepository.findByTitle(dto.getSeriesName()).orElse(null);
        if (targetSeries == null) {
            targetSeries = seriesRepository.saveAndFlush(Series.builder()
                    .title(dto.getSeriesName())
                            .description("아직 작성된 설명이 없습니다.")
                    .build());
        }

        var targetPost = postRepository.findByTitle(dto.getTitle())
                .orElse(null);

        Post resultPost;
        //신규 생성 모드로
        if (targetPost == null) {
            resultPost = createNewPostWithFile(targetSeries, dto);
        }
        //수정 모드
        else{
            resultPost = updatePostWithFile(targetSeries, dto,targetPost);
        }

        return new PostResponseServiceDto(resultPost, dto.getContent(), true);
    }
    @Transactional
    public Post createNewPostWithFile(Series targetSeries, PostUploadRequestServiceDto dto) {
        var newPost = Post.builder()
                .createAt(timeUtility.nowDateTime())
                .updateAt(timeUtility.nowDateTime())
                .series(targetSeries)
                .thumbnail(dto.getThumbnail())
                .title(dto.getTitle())
                .build();
        var savedPost = postRepository.saveAndFlush(newPost);

        //create post content
        var renderContent = markdownConvertor.convertToHtml(dto.getContent());
        var newPostContent = postContentRepository.save(PostContent
                .builder()
                .post(newPost)
                .text(dto.getContent())
                .build()
        );
        var newPostConvertContent = postConvertContentRepository.save(PostConvertContent
                .builder()
                .post(newPost)
                .text(renderContent)
                .build()
        );

        //create post index
        var plainText = markdownConvertor.convertToPlainText(newPostConvertContent.getText(), true);
        var newPostIndex = newPost.toIndex(plainText);
        postIndexRepository.save(newPostIndex);

        return savedPost;
    }

    @Transactional
    public Post updatePostWithFile(Series targetSeries, PostUploadRequestServiceDto dto, Post targetPost){
        var postConvertContent = postConvertContentRepository.findByPost(targetPost)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.UNKNOWN_ERROR, "변환된 포스트 내용을 찾을 수 없습니다."));
        var postContent = postContentRepository.findByPost(targetPost)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.UNKNOWN_ERROR, "포스트 내용을 찾을 수 없습니다."));

        targetPost.update(dto.getTitle(), dto.getThumbnail(), timeUtility.nowDateTime() ,targetSeries);
        var savedPost = postRepository.saveAndFlush(targetPost);

        var updatedRenderContent = markdownConvertor.convertToHtml(dto.getContent());
        postConvertContent.update(updatedRenderContent);
        var updatedPostConvertContent = postConvertContentRepository.saveAndFlush(postConvertContent);

        //update content
        postContent.update(dto.getContent());
        var updatedPostContent = postContentRepository.saveAndFlush(postContent);


        //update post index
        var postIndex = savedPost.toIndex(markdownConvertor.convertToPlainText(updatedPostConvertContent.getText(), true));
        postIndexRepository.save(postIndex);

        return savedPost;
    }
    @Transactional
    public PostResponseServiceDto updatePost(PostUpdateRequestServiceDto dto) {
        var targetPost = postRepository.findById(dto.getId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "포스트를 찾을 수 없습니다."));

        var targetSeries = seriesRepository.findById(dto.getSeriesId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "시리즈를 찾을 수 없습니다."));

        //생성시 들어가는 본문 내용들이므로, 찾을 수 없으면 서버 내부에 에러가 발생한 상황임.
        var postConvertContent = postConvertContentRepository.findByPost(targetPost)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.UNKNOWN_ERROR, "변환된 포스트 내용을 찾을 수 없습니다."));
        var postContent = postContentRepository.findByPost(targetPost)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.UNKNOWN_ERROR, "포스트 내용을 찾을 수 없습니다."));

        if (!dto.getTitle().equals(targetPost.getTitle())) {
            checkExistTitle(dto.getTitle());
        }


        //update post
        targetPost.update(dto.toEntity(targetSeries, timeUtility.nowDateTime()));
        var updatedPost = postRepository.saveAndFlush(targetPost);

        //update render content
        var updatedRenderContent = markdownConvertor.convertToHtml(dto.getContent());
        postConvertContent.update(updatedRenderContent);
        var updatedPostConvertContent = postConvertContentRepository.saveAndFlush(postConvertContent);

        //update content
        postContent.update(dto.getContent());
        var updatedPostContent = postContentRepository.saveAndFlush(postContent);


        //update post index
        var postIndex = updatedPost.toIndex(markdownConvertor.convertToPlainText(updatedPostConvertContent.getText(), true));
        postIndexRepository.save(postIndex);

        return new PostResponseServiceDto(updatedPost, updatedPostConvertContent.getText(), true);
    }

    @Transactional
    public PostResponseServiceDto deletePost(Long id) {
        var targetPost = postRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "포스트를 찾을 수 없습니다."));

        postRepository.delete(targetPost);

        var postIndex = postIndexRepository.findById(id).orElse(null);

        if (postIndex != null) postRepository.delete(targetPost);

        var content = (postIndex == null) ? "not found" : postIndex.getContent();
        return new PostResponseServiceDto(targetPost, content, false);

    }


    @Transactional(readOnly = true)
    public Page<PostResponseDto> getAllPost(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getAllPostWithSeries(Pageable pageable, Long seriesId) {
        var targetSeries = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "시리즈를 찾을 수 없습니다."));
        return postRepository.findAllBySeries(pageable, targetSeries)
                .map(PostResponseDto::new);
    }


    @Transactional(readOnly = true)
    public Page<PostResponseDto> getAllPostWithIndex(Pageable pageable, String title, String content) {
        var searchType = getSearchType(title, content);
        Page<PostIndex> pagePostIndex;

        switch (searchType) {
            case TITLE -> pagePostIndex = postIndexRepository.findAllByTitleLike(title, pageable);
            case ALL -> pagePostIndex = postIndexRepository.findAllByTitleLikeOrContentLike(title, content, pageable);
            default -> pagePostIndex = postIndexRepository.findAllByContentLike(content, pageable);
        }

        var targetIds = pagePostIndex.getContent().stream().map(PostIndex::getId).toList();

        var posts = postRepository.findAllByIdIn(targetIds)
                .stream()
                .map(PostResponseDto::new)
                .toList();

        return new PageImpl<>(posts, pageable, pagePostIndex.getTotalElements());

    }

    @Transactional(readOnly = true)
    public void checkExistTitle(String title) {
        if (postRepository.existsByTitle(title)) {
            throw new ServiceException(ServiceErrorCode.ALREADY_EXIST, "이미 존재하는 제목입니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getRecommendPost() {
        var postIds = Arrays.stream(configService.getConfig("RECOMMEND_POST_IDS")
                .getValue()
                .split(","))
                .map(Long::parseLong)
                .toList();

        return postRepository.findAllByIdIn(postIds)
                .stream()
                .map(PostResponseDto::new)
                .toList();
    }

    private SearchType getSearchType(String title, String content) {
        if (!title.equals("") && !content.equals("")) {
            return SearchType.ALL;
        } else if (!title.equals("")) {
            return SearchType.TITLE;
        } else if (!content.equals("")) {
            return SearchType.DESCRIPTION;
        } else {
            throw new ServiceException(ServiceErrorCode.UNKNOWN_ERROR, "검색 조건이 없습니다.");
        }
    }


    private enum SearchType {
        TITLE, DESCRIPTION, ALL
    }

}
