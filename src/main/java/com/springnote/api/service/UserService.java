package com.springnote.api.service;

import com.springnote.api.domain.jpa.user.UserRepository;
import com.springnote.api.dto.user.common.UserResponseDto;
import com.springnote.api.utils.exception.service.ServiceErrorCode;
import com.springnote.api.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto findUserById(String id) {
        return userRepository.findById(id)
                .map(UserResponseDto::new)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND,"해당 유저를 찾을 수 없습니다."));
    }
}
