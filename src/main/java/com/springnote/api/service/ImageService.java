package com.springnote.api.service;

import com.springnote.api.domain.jpa.image.Image;
import com.springnote.api.domain.jpa.image.ImageRepository;
import com.springnote.api.dto.image.service.ImageResponseServiceDto;
import com.springnote.api.utils.exception.service.ServiceErrorCode;
import com.springnote.api.utils.exception.service.ServiceException;
import com.springnote.api.utils.time.TimeUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final TimeUtility timeUtility;
    private final ImageRepository imageRepository;
    private final FileService fileService;

    @Transactional(readOnly = true)
    public byte[] findImageById(Long id) {
        var target = imageRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "이미지를 찾을 수 없습니다."));
        return fileService.findFile(target.getName());
    }

    @Transactional
    public ImageResponseServiceDto createImage(MultipartFile file) {
        var convertImage = fileService.convertImage(file);
        var image = Image
                .builder()
                .createAt(timeUtility.nowDateTime())
                .originName(file.getOriginalFilename())
                .name(convertImage.getFullFileName())
                .build();

        var savedImage = imageRepository.save(image);

        return new ImageResponseServiceDto(savedImage);
    }

    @Transactional
    public ImageResponseServiceDto deleteImage(Long id) {
        var target = imageRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "이미지를 찾을 수 없습니다."));

        imageRepository.delete(target);
        fileService.deleteImage(target.getName());
        return new ImageResponseServiceDto(target);
    }
}
