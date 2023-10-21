package com.springnote.api.service;

import com.springnote.api.domain.jpa.config.ConfigRepository;
import com.springnote.api.dto.config.common.ConfigResponseDto;
import com.springnote.api.utils.exception.service.ServiceErrorCode;
import com.springnote.api.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfigService {
    private final ConfigRepository configRepository;

    public ConfigResponseDto getConfig(String key) {
        var config = configRepository.findById(key)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.UNKNOWN_ERROR,"설정을 찾을 수 없습니다."));

        return ConfigResponseDto.builder()
                .key(config.getKey())
                .value(config.getKey())
                .build();
    }
}
