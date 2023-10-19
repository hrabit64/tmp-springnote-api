package com.springnote.api.service;

import com.springnote.api.dto.image.service.FileResponseServiceDto;
import com.springnote.api.utils.context.RequestIdContext;
import com.springnote.api.utils.exception.service.ServiceErrorCode;
import com.springnote.api.utils.exception.service.ServiceException;
import com.springnote.api.utils.file.ConvertedFile;
import com.springnote.api.utils.file.ImageManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    private final ImageManager imageManager;
    private final RequestIdContext requestIdContext;
    public FileResponseServiceDto convertImage(MultipartFile file) {
        try {
            var originFileFullName = file.getOriginalFilename();

            var lastDotIndex = originFileFullName.lastIndexOf(".");
            if (lastDotIndex == -1) {
                throw new ServiceException(ServiceErrorCode.NOT_VALID, "올바르지 않은 파일 형식입니다.");
            }
            var originFileName = originFileFullName.substring(0, lastDotIndex);
            var originFileType = originFileFullName.substring(lastDotIndex + 1);

            var newFileName = UUID.randomUUID().toString();

            var convertedFile = imageManager.optimize(file, newFileName, originFileType);

            var fileFullName = convertedFile.getName();
            var fileName = fileFullName.substring(0, fileFullName.lastIndexOf("."));

            return FileResponseServiceDto.builder()
                    .fileName(fileName)
                    .fileType(originFileType)
                    .build();
        } catch (IOException e) {
            log.debug(e.getLocalizedMessage());
            throw new ServiceException(ServiceErrorCode.UNKNOWN_ERROR, "이미지 변환에 실패했습니다.");
        } catch (Exception e) {
            log.debug(e.getClass().getName());
            throw new ServiceException(ServiceErrorCode.UNKNOWN_ERROR, "알 수 없는 에러가 발생했습니다.");
        }
    }

    public void deleteImage(String fileName) {
        try{
            var result = imageManager.delete(fileName);
            assert result;
        } catch (Exception e) {
            log.warn("File Service request id: {} delete image error", requestIdContext.getId());
        }
    }

    public byte[] findFile(String name) {
        try {
            var file = imageManager.find(name);
            return Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            log.warn("File Service request id: {} find file error", requestIdContext.getId());
            throw new ServiceException(ServiceErrorCode.UNKNOWN_ERROR, "파일을 찾을 수 없습니다.");
        }
    }
}
