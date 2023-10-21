package com.springnote.api.utils.modelAssembler;

import com.springnote.api.dto.image.controller.ImageResponseControllerDto;
import com.springnote.api.dto.series.controller.SeriesUpdateRequestControllerDto;
import com.springnote.api.web.controller.ImageApiController;
import com.springnote.api.web.controller.SeriesApiController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ImageResponseControllerDtoAssembler implements RepresentationModelAssembler<ImageResponseControllerDto, EntityModel<ImageResponseControllerDto>> {

    @Nonnull
    @Override
    public EntityModel<ImageResponseControllerDto> toModel(@Nonnull ImageResponseControllerDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ImageApiController.class).getImageById(entity.getId())).withSelfRel(),
                linkTo(methodOn(ImageApiController.class).deleteImage(entity.getId())).withRel("delete"));
    }


}
