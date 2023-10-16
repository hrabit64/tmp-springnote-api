package com.springnote.api.utils.dtoModelAssembler;

import com.springnote.api.dto.user.common.UserResponseDto;


import com.springnote.api.dto.user.controller.UserUpdateRequestControllerDto;
import com.springnote.api.web.controller.UserApiController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserResponseDtoAssembler implements RepresentationModelAssembler<UserResponseDto, EntityModel<UserResponseDto>> {

    @Nonnull
    @Override
    public EntityModel<UserResponseDto> toModel(@Nonnull UserResponseDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserApiController.class).getUser(entity.getUid())).withSelfRel(),
                linkTo(methodOn(UserApiController.class).updateUser(entity.getUid(), new UserUpdateRequestControllerDto())).withRel("update"),
                linkTo(methodOn(UserApiController.class).withdrawUser(entity.getUid())).withRel("withdraw"));
    }
}
