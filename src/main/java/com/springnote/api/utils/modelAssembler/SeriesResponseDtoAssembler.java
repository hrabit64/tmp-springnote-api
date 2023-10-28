package com.springnote.api.utils.modelAssembler;

import com.springnote.api.dto.series.common.SeriesResponseDto;
import com.springnote.api.dto.series.controller.SeriesUpdateRequestControllerDto;
import com.springnote.api.web.controller.SeriesApiController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SeriesResponseDtoAssembler implements RepresentationModelAssembler<SeriesResponseDto, EntityModel<SeriesResponseDto>> {

    @Nonnull
    @Override
    public EntityModel<SeriesResponseDto> toModel(@Nonnull SeriesResponseDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(SeriesApiController.class).getSeriesById(entity.getId())).withSelfRel());
    }


}
