package com.springnote.api.service;

import com.springnote.api.domain.jpa.config.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfigService {
    private final ConfigRepository configRepository;
}
