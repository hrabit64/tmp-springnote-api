package com.springnote.api.utils.modelAssembler;

import com.springnote.api.dto.post.common.PostResponseDto;
import com.springnote.api.dto.post.controller.PostResponseControllerDto;
import com.springnote.api.dto.post.controller.PostUpdateRequestControllerDto;
import com.springnote.api.web.controller.PostApiController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostResponseDtoAssembler implements RepresentationModelAssembler<PostResponseDto, EntityModel<PostResponseDto>> {

    @Nonnull
    @Override
    public EntityModel<PostResponseDto> toModel(@Nonnull PostResponseDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PostApiController.class).getPostById(entity.getId(),true)).withSelfRel(),
                linkTo(methodOn(PostApiController.class).updatePost(entity.getId(), new PostUpdateRequestControllerDto())).withRel("update"),
                linkTo(methodOn(PostApiController.class).deletePost(entity.getId())).withRel("delete"));
    }


}
