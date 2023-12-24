package com.springnote.api.utils.modelAssembler;

import com.springnote.api.dto.post.controller.PostResponseControllerDto;
import com.springnote.api.dto.post.controller.PostUpdateRequestControllerDto;
import com.springnote.api.dto.series.common.SeriesResponseDto;
import com.springnote.api.dto.series.controller.SeriesUpdateRequestControllerDto;
import com.springnote.api.web.controller.PostApiController;
import com.springnote.api.web.controller.SeriesApiController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostResponseControllerDtoAssembler implements RepresentationModelAssembler<PostResponseControllerDto, EntityModel<PostResponseControllerDto>> {

    @Nonnull
    @Override
    public EntityModel<PostResponseControllerDto> toModel(@Nonnull PostResponseControllerDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PostApiController.class).getPostById(entity.getId())).withSelfRel(),
                linkTo(methodOn(PostApiController.class).deletePost(entity.getId())).withRel("delete"));
    }


}
