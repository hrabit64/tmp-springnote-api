package com.springnote.api.dto.image.service;

import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponseServiceDto {
    private String fileName;
    private String fileType;

    public String getFullFileName() {
        return fileName + "." + fileType;
    }
}
