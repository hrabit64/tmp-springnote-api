package com.springnote.api.service;

import com.springnote.api.domain.jpa.user.UserRepository;
import com.springnote.api.dto.user.common.UserResponseDto;
import com.springnote.api.dto.user.service.UserCreateRequestServiceDto;
import com.springnote.api.dto.user.service.UserUpdateRequestServiceDto;
import com.springnote.api.utils.exception.service.ServiceErrorCode;
import com.springnote.api.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponseDto findUserById(String id) {
        return userRepository.findById(id)
                .map(UserResponseDto::new)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));
    }

    @Transactional
    public UserResponseDto createUser(UserCreateRequestServiceDto dto) {

        var newUser = dto.toEntity();

        checkNameExist(newUser.getName());

        var savedUser = userRepository.save(newUser);
        return new UserResponseDto(savedUser);
    }

    @Transactional
    public UserResponseDto updateUser(UserUpdateRequestServiceDto dto) {
        var updateUser = dto.toEntity();

        var targetUser = userRepository.findById(dto.getUid())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        if (!targetUser.getName().equals(updateUser.getName())) checkNameExist(updateUser.getName());

        targetUser.update(updateUser);

        var savedUser = userRepository.save(targetUser);

        return new UserResponseDto(savedUser);
    }

    @Transactional
    public UserResponseDto deleteUser(String uid) {

        var targetUser = userRepository.findById(uid)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        userRepository.delete(targetUser);

        return new UserResponseDto(targetUser);
    }

    @Transactional(readOnly = true)
    public void checkNameExist(String name) {
        if (userRepository.existsUserByName(name)) {
            throw new ServiceException(ServiceErrorCode.ALREADY_EXIST, "이미 존재하는 이름입니다.");
        }
    }
}
