package com.springnote.api.dto.image.service;

import com.springnote.api.domain.jpa.image.Image;
import com.springnote.api.dto.image.controller.ImageResponseControllerDto;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponseServiceDto {

    private Long id;
    private String originName;
    private String name;

    public ImageResponseServiceDto(Image image){
        id = image.getId();
        originName = image.getOriginName();
        name = image.getName();
    }

    public ImageResponseControllerDto toControllerDto(){
        return ImageResponseControllerDto.builder()
                .id(id)
                .name(name)
                .build();
    }
}
