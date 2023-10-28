//package com.springnote.api.utils.modelAssembler;
//
//import com.springnote.api.dto.comment.common.CommentResponseDto;
//import com.springnote.api.dto.image.controller.ImageResponseControllerDto;
//import com.springnote.api.web.controller.CommentApiController;
//import com.springnote.api.web.controller.ImageApiController;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.hateoas.server.RepresentationModelAssembler;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Nonnull;
//
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
//
//@Component
//public class CommentResponseDtoAssembler implements RepresentationModelAssembler<CommentResponseDto, EntityModel<CommentResponseDto>> {
//
//    @Nonnull
//    @Override
//    public EntityModel<CommentResponseDto> toModel(@Nonnull CommentResponseDto entity) {
//        return EntityModel.of(entity,
//                linkTo(methodOn(CommentApiController.class).getCommentById(entity.getId())).withSelfRel(),
//                linkTo(methodOn(CommentApiController.class).deleteComment(entity.getId())).withRel("delete"));
//    }
//
//
//}
