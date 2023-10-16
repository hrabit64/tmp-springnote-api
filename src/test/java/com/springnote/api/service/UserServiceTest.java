package com.springnote.api.service;

import com.springnote.api.domain.jpa.user.User;
import com.springnote.api.domain.jpa.user.UserRepository;
import com.springnote.api.dto.user.common.UserResponseDto;

import com.springnote.api.dto.user.service.UserCreateRequestServiceDto;
import com.springnote.api.dto.user.service.UserUpdateRequestServiceDto;
import com.springnote.api.utils.exception.service.ServiceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @DisplayName("findUserById - 존재하는 id(thisIsNotRealUidILikeHos1234)가 주어지면, 해당 user를 반환한다.")
    @Test
    void findUserById() {
        //given
        var existId = "thisIsNotRealUidILikeHos1234";

        var exceptedUser = User.builder()
                .id(existId)
                .name("test")
                .isAdmin(false)
                .build();

        var excepted = UserResponseDto.builder()
                .uid(existId)
                .name("test")
                .isAdmin(false)
                .build();

        doReturn(Optional.of(exceptedUser)).when(userRepository).findById(existId);
        //when
        var result = userService.findUserById(existId);

        //then
        verify(userRepository).findById(existId);
        assertEquals(excepted, result);
    }

    @DisplayName("findUserById - 존재하지 않는 id(thisIsNotRealUidILikeHos5555)가 주어지면, ServiceException이 발생한다.")
    @Test
    void findUserById2() {
        //given
        var notExistId = "thisIsNotRealUidILikeHos5555";



        doReturn(Optional.empty()).when(userRepository).findById(notExistId );
        //when
        var exception = assertThrows(ServiceException.class, () -> userService.findUserById(notExistId));
        assertEquals("해당 유저를 찾을 수 없습니다.", exception.getMessage());

        //then
        verify(userRepository).findById(notExistId);
    }

    @DisplayName("createUser - 올바른 정보가 주어지면, 해당 유저를 생성하고 생성된 유저의 정보를 반환한다.")
    @Test
    void createUser() {
        //given
        var requestDto = UserCreateRequestServiceDto.builder()
                .uid("thisIsNotRealUidILikeHos1234")
                .name("test")
                .isAdmin(false)
                .build();

        var exceptedUser = User.builder()
                .id("thisIsNotRealUidILikeHos1234")
                .name("test")
                .isAdmin(false)
                .build();

        var excepted = UserResponseDto
                .builder()
                .uid("thisIsNotRealUidILikeHos1234")
                .name("test")
                .isAdmin(false)
                .build();

        doReturn(exceptedUser).when(userRepository).save(exceptedUser);
        doReturn(false).when(userRepository).existsUserByName("test");
        //when
        var result = userService.createUser(requestDto);

        //then
        verify(userRepository).save(exceptedUser);
        verify(userRepository).existsUserByName("test");
        assertEquals(excepted, result);
    }

    @DisplayName("createUser - 중복되는 이름을 가진 유저 정보가 주어지면, ServiceException이 발생한다.")
    @Test
    void createUser2() {
        //given
        var requestDto = UserCreateRequestServiceDto.builder()
                .uid("thisIsNotRealUidILikeHos1234")
                .name("test")
                .isAdmin(false)
                .build();

        doReturn(true).when(userRepository).existsUserByName("test");
        //when
        var exception = assertThrows(ServiceException.class, () -> userService.createUser(requestDto));
        assertEquals("이미 존재하는 이름입니다.", exception.getMessage());

        //then
        verify(userRepository).existsUserByName("test");
    }

    @DisplayName("updateUser - 존재하는 id(thisIsNotRealUidILikeHos1234)와 올바른 정보가 주어지면, 해당 유저를 수정하고 수정된 유저의 정보를 반환한다.")
    @Test
    void updateUser() {
        //given
        var existId = "thisIsNotRealUidILikeHos1234";

        var requestDto = UserUpdateRequestServiceDto.builder()
                .uid(existId)
                .name("test123")
                .build();

        var targetUser = User.builder()
                .id(existId)
                .name("test")
                .isAdmin(false)
                .build();

        var updatedUser = User.builder()
                .id(existId)
                .name("test123")
                .isAdmin(false)
                .build();

        var excepted = UserResponseDto.builder()
                .uid(existId)
                .name("test123")
                .isAdmin(false)
                .build();

        doReturn(Optional.of(targetUser)).when(userRepository).findById(existId);
        doReturn(false).when(userRepository).existsUserByName("test123");
        doReturn(updatedUser).when(userRepository).save(updatedUser);

        //when
        var result = userService.updateUser(requestDto);

        //then
        verify(userRepository).findById(existId);
        verify(userRepository).existsUserByName("test123");
        verify(userRepository).save(updatedUser);
        assertEquals(excepted, result);
    }

    @DisplayName("updateUser - 존재하는 id(thisIsNotRealUidILikeHos1234)와 중복되는 이름이 주어지면, ServiceException이 발생한다.")
    @Test
    void updateUser2() {
        //given
        var existId = "thisIsNotRealUidILikeHos1234";

        var requestDto = UserUpdateRequestServiceDto.builder()
                .uid(existId)
                .name("test123")
                .build();

        var targetUser = User.builder()
                .id(existId)
                .name("test")
                .isAdmin(false)
                .build();



        doReturn(Optional.of(targetUser)).when(userRepository).findById(existId);
        doReturn(true).when(userRepository).existsUserByName("test123");

        //when
        var exception = assertThrows(ServiceException.class, () -> userService.updateUser(requestDto));
        assertEquals("이미 존재하는 이름입니다.", exception.getMessage());

        //then
        verify(userRepository).findById(existId);
        verify(userRepository).existsUserByName("test123");

    }

    @DisplayName("updateUser - 존재하지 않는 id(thisIsNotRealUidILikeHos5555)가 주어지면, ServiceException이 발생한다.")
    @Test
    void updateUser3() {
        //given
        var notExistId = "thisIsNotRealUidILikeHos5555";

        var requestDto = UserUpdateRequestServiceDto.builder()
                .uid(notExistId)
                .name("test123")
                .build();

        doReturn(Optional.empty()).when(userRepository).findById(notExistId);

        //when
        var exception = assertThrows(ServiceException.class, () -> userService.updateUser(requestDto));
        assertEquals("해당 유저를 찾을 수 없습니다.", exception.getMessage());

        //then
        verify(userRepository).findById(notExistId);

    }

    @DisplayName("deleteUser - 존재하는 id(thisIsNotRealUidILikeHos1234)가 주어지면, 유저를 삭제하고, 삭제된 유저를 반환한다.")
    @Test
    void deleteUser() {
        //given
        var existId = "thisIsNotRealUidILikeHos1234";

        var targetUser = User.builder()
                .id(existId)
                .name("test")
                .isAdmin(false)
                .build();

        var excepted = UserResponseDto.builder()
                .uid(existId)
                .name("test")
                .isAdmin(false)
                .build();

        doReturn(Optional.of(targetUser)).when(userRepository).findById(existId);


        //when
        var result = userService.deleteUser(existId);

        //then
        verify(userRepository).findById(existId);
        verify(userRepository).delete(targetUser);
        assertEquals(excepted, result);
    }

    @DisplayName("deleteUser - 존재하지 않는 id(thisIsNotRealUidILikeHos5555)가 주어지면, ServiceException이 발생한다.")
    @Test
    void deleteUser2() {
        //given
        var notExistId = "thisIsNotRealUidILikeHos5555";


        doReturn(Optional.empty()).when(userRepository).findById(notExistId);

        //when
        var exception = assertThrows(ServiceException.class, () -> userService.deleteUser(notExistId));
        assertEquals("해당 유저를 찾을 수 없습니다.", exception.getMessage());

        //then
        verify(userRepository).findById(notExistId);
    }
}