package com.springnote.api.dto.post.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.springnote.api.dto.post.common.PostResponseDto;
import lombok.*;
import org.springframework.hateoas.EntityModel;

import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PostResponseListControllerDto {
    private List<EntityModel<PostResponseDto>> items;
}
