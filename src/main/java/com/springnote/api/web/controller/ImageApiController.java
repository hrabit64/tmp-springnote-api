package com.springnote.api.web.controller;

import com.springnote.api.dto.image.controller.ImageResponseControllerDto;
import com.springnote.api.service.FileService;
import com.springnote.api.service.ImageService;
import com.springnote.api.utils.aop.auth.AuthLevel;
import com.springnote.api.utils.aop.auth.EnableAuth;
import com.springnote.api.utils.file.ImageManager;
import com.springnote.api.utils.modelAssembler.ImageResponseControllerDtoAssembler;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/image")
public class ImageApiController {

    private final ImageService imageService;
    private final ImageResponseControllerDtoAssembler assembler;

    @GetMapping(value = "/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImageById(@PathVariable Long imageId) {
        return imageService.findImageById(imageId);
    }

    //    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @PostMapping("")
    public EntityModel<ImageResponseControllerDto> createImage(@RequestParam("file") @NotNull MultipartFile file) {
        var fileResult = imageService.createImage(file);
        return assembler.toModel(fileResult.toControllerDto());
    }

    //    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @DeleteMapping("/{imageId}")
    public EntityModel<ImageResponseControllerDto> deleteImage(@PathVariable Long imageId) {
        var fileResult = imageService.deleteImage(imageId);
        return assembler.toModel(fileResult.toControllerDto());
    }

}
