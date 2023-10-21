package com.springnote.api.dto.config.common;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigResponseDto {
    private String key;
    private String value;
}
