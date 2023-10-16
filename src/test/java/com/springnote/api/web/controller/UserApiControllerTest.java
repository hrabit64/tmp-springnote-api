package com.springnote.api.web.controller;

import com.google.firebase.FirebaseException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.springnote.api.dto.firebase.service.FirebaseResponse;
import com.springnote.api.dto.user.common.UserResponseDto;
import com.springnote.api.dto.user.controller.UserRegisterRequestControllerDto;
import com.springnote.api.dto.user.controller.UserUpdateRequestControllerDto;
import com.springnote.api.dto.user.service.UserCreateRequestServiceDto;
import com.springnote.api.dto.user.service.UserUpdateRequestServiceDto;
import com.springnote.api.service.FirebaseService;
import com.springnote.api.service.UserService;
import com.springnote.api.utils.context.UserContext;
import com.springnote.api.utils.exception.service.ServiceErrorCode;
import com.springnote.api.utils.exception.service.ServiceException;
import com.springnote.api.utils.json.LocalDateSerializer;

import com.springnote.api.utils.json.LocalDateTimeSerializer;
import com.springnote.api.utils.json.SnakeCaseFieldNamingStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;


import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @LocalServerPort
    private int port;

    @MockBean
    private UserService userService;

    @MockBean
    private FirebaseService firebaseService;


    private final Gson gson = new GsonBuilder()
            .setFieldNamingStrategy(new SnakeCaseFieldNamingStrategy())
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
            .serializeNulls()
            .create();

    @DisplayName("registerUser - 올바른 정보가 주어지면, 회원을 등록하고 그 결과를 리턴한다.")
    @Test
    void registerUser() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";
        var testTokenInfo = FirebaseResponse.builder()
                .uid("thisIsTestUid")
                .build();

        var requestDto = UserRegisterRequestControllerDto.builder()
                .name("testName")
                .build();

        var exceptResult = UserResponseDto.builder()
                .uid("thisIsTestUid")
                .name("testName")
                .isAdmin(false)
                .build();


        doReturn(testTokenInfo).when(firebaseService).decodeToken(testToken);

        doReturn(exceptResult).when(userService).createUser(UserCreateRequestServiceDto.builder()
                .isAdmin(false)
                .name("testName")
                .uid("thisIsTestUid")
                .build());
        //when
        var url = "http://localhost:" + port + "/api/v1/user/register";
        var result = mvc.perform(
                        post(url)
                                .header("Authorization", "Bearer " + testToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(requestDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("thisIsTestUid"))
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.admin").value(false));
        verify(firebaseService).decodeToken(testToken);
        verify(userService).createUser(UserCreateRequestServiceDto.builder()
                .isAdmin(false)
                .name("testName")
                .uid("thisIsTestUid")
                .build());
    }

    @DisplayName("registerUser - 이름 글자수를 초과하면, 400 에러를 리턴한다.")
    @Test
    void registerUser2() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";
        var requestDto = UserRegisterRequestControllerDto.builder()
                .name("testName12312312312")
                .build();

        //when
        var url = "http://localhost:" + port + "/api/v1/user/register";
        var result = mvc.perform(
                        post(url)
                                .header("Authorization", "Bearer " + testToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(requestDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        result.andExpect(status().isBadRequest());
    }
//
//    @DisplayName("registerUser - Bearer 토큰이 없으면, 403 에러를 리턴한다.")
//    @Test
//    void registerUser3() throws Exception {
//        //given
//        var testToken = "thisIsTestIdToken";
//        var requestDto = UserRegisterRequestControllerDto.builder()
//                .name("testName")
//                .build();
//
//        //when
//        var url = "http://localhost:" + port + "/api/v1/user/register";
//        var result = mvc.perform(
//                        post(url)
//                                .header("Authorization", testToken)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(gson.toJson(requestDto))
//                                .accept(MediaType.APPLICATION_JSON))
//                .andDo(print());
//
//        //then
//        result.andExpect(status().isForbidden());
//    }
//
//    @DisplayName("registerUser - 인증에 실패하면, 403 에러를 리턴한다.")
//    @Test
//    void registerUser4() throws Exception {
//        //given
//        var testToken = "thisIsTestIdToken";
//        var requestDto = UserRegisterRequestControllerDto.builder()
//                .name("testName")
//                .build();
//
//        doThrow(new ServiceException(ServiceErrorCode.NOT_FOUND,"해당 토큰을 디코딩했으나, 유효한 정보를 찾을 수 없습니다. 유효하지 않거나 만료된 토큰입니다.")).when(firebaseService).decodeToken(testToken);
//
//        //when
//        var url = "http://localhost:" + port + "/api/v1/user/register";
//        var result = mvc.perform(
//                        post(url)
//                                .header("Authorization", "Bearer " + testToken)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(gson.toJson(requestDto))
//                                .accept(MediaType.APPLICATION_JSON))
//                .andDo(print());
//
//        //then
//        result.andExpect(status().isForbidden());
//        verify(firebaseService).decodeToken(testToken);
//    }

    @DisplayName("getUser - 일반회원이 자신의 정보를 조회하면, 회원을 조회하고 그 결과를 리턴한다.")
    @Test
    void getUser() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";
        var testUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid")
                .name("testName")
                .isAdmin(false)
                .build();

        doReturn(testUserResponseDto).when(firebaseService).findUserByToken(testToken);
        doReturn(testUserResponseDto).when(userService).findUserById("thisIsTestUid");

        //when
        var url = "http://localhost:" + port + "/api/v1/user/thisIsTestUid";
        var result = mvc.perform(
                        get(url)
                                .header("Authorization", "Bearer " + testToken)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("thisIsTestUid"))
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.admin").value(false));
        verify(firebaseService).findUserByToken(testToken);
        verify(userService).findUserById("thisIsTestUid");
    }

    @DisplayName("getUser - 관리자가 타인의 정보를 조회하면, 회원을 조회하고 그 결과를 리턴한다.")
    @Test
    void getUser2() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";
        var testUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid2")
                .name("testName2")
                .isAdmin(true)
                .build();

        var testTargetUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid")
                .name("testName")
                .isAdmin(false)
                .build();

        doReturn(testUserResponseDto).when(firebaseService).findUserByToken(testToken);
        doReturn(testTargetUserResponseDto).when(userService).findUserById("thisIsTestUid");

        //when
        var url = "http://localhost:" + port + "/api/v1/user/thisIsTestUid";
        var result = mvc.perform(
                        get(url)
                                .header("Authorization", "Bearer " + testToken)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("thisIsTestUid"))
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.admin").value(false));
        verify(firebaseService).findUserByToken(testToken);
        verify(userService).findUserById("thisIsTestUid");
    }

    @DisplayName("getUser - 관리자가 자신의 정보를 조회하면, 회원을 조회하고 그 결과를 리턴한다.")
    @Test
    void getUser3() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";
        var testUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid")
                .name("testName")
                .isAdmin(true)
                .build();

        doReturn(testUserResponseDto).when(firebaseService).findUserByToken(testToken);
        doReturn(testUserResponseDto).when(userService).findUserById("thisIsTestUid");

        //when
        var url = "http://localhost:" + port + "/api/v1/user/thisIsTestUid";
        var result = mvc.perform(
                        get(url)
                                .header("Authorization", "Bearer " + testToken)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("thisIsTestUid"))
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.admin").value(true));
        verify(firebaseService).findUserByToken(testToken);
        verify(userService).findUserById("thisIsTestUid");
    }

    @DisplayName("getUser - 일반회원이 타인의 정보를 조회하면, 403을 리턴한다.")
    @Test
    void getUser4() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";
        var testUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid")
                .name("testName")
                .isAdmin(false)
                .build();

        doReturn(testUserResponseDto).when(firebaseService).findUserByToken(testToken);

        //when
        var url = "http://localhost:" + port + "/api/v1/user/thisIsTestUid2";
        var result = mvc.perform(
                        get(url)
                                .header("Authorization", "Bearer " + testToken)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        result.andExpect(status().isForbidden());
        verify(firebaseService).findUserByToken(testToken);
    }

    @DisplayName("withdrawUser - 일반회원이 자신의 정보를 삭제하면, 회원을 삭제하고 그 결과를 리턴한다.")
    @Test
    void withdrawUser() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";
        var testUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid")
                .name("testName")
                .isAdmin(false)
                .build();

        doReturn(testUserResponseDto).when(firebaseService).findUserByToken(testToken);
        doReturn(testUserResponseDto).when(userService).deleteUser("thisIsTestUid");
        //when
        var url = "http://localhost:" + port + "/api/v1/user/thisIsTestUid";
        var result = mvc.perform(
                        delete(url)
                                .header("Authorization", "Bearer " + testToken)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("thisIsTestUid"))
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.admin").value(false));

        verify(firebaseService).findUserByToken(testToken);
        verify(userService).deleteUser("thisIsTestUid");

    }

    @DisplayName("withdrawUser - 관리자 회원이 자신의 정보를 삭제하면, 회원을 삭제하고 그 결과를 리턴한다.")
    @Test
    void withdrawUser2() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";
        var testUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid")
                .name("testName")
                .isAdmin(true)
                .build();

        doReturn(testUserResponseDto).when(firebaseService).findUserByToken(testToken);
        doReturn(testUserResponseDto).when(userService).deleteUser("thisIsTestUid");
        //when
        var url = "http://localhost:" + port + "/api/v1/user/thisIsTestUid";
        var result = mvc.perform(
                        delete(url)
                                .header("Authorization", "Bearer " + testToken)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("thisIsTestUid"))
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.admin").value(true));
        verify(firebaseService).findUserByToken(testToken);
        verify(userService).deleteUser("thisIsTestUid");
    }

    @DisplayName("withdrawUser - 관리자 회원이 타인의 정보를 삭제하면, 회원을 삭제하고 그 결과를 리턴한다.")
    @Test
    void withdrawUser3() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";
        var testUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid2")
                .name("testName2")
                .isAdmin(true)
                .build();

        var testTargetUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid")
                .name("testName")
                .isAdmin(false)
                .build();

        doReturn(testUserResponseDto).when(firebaseService).findUserByToken(testToken);
        doReturn(testTargetUserResponseDto).when(userService).deleteUser("thisIsTestUid");
        //when
        var url = "http://localhost:" + port + "/api/v1/user/thisIsTestUid";
        var result = mvc.perform(
                        delete(url)
                                .header("Authorization", "Bearer " + testToken)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("thisIsTestUid"))
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.admin").value(false));
        verify(firebaseService).findUserByToken(testToken);
        verify(userService).deleteUser("thisIsTestUid");
    }

    @DisplayName("withdrawUser - 일반 회원이 타인의 정보를 삭제하면, 403을 리턴한다.")
    @Test
    void withdrawUser4() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";
        var testUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid2")
                .name("testName2")
                .isAdmin(false)
                .build();


        doReturn(testUserResponseDto).when(firebaseService).findUserByToken(testToken);

        //when
        var url = "http://localhost:" + port + "/api/v1/user/thisIsTestUid";
        var result = mvc.perform(
                        delete(url)
                                .header("Authorization", "Bearer " + testToken)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        result.andExpect(status().isForbidden());
        verify(firebaseService).findUserByToken(testToken);
    }

    @DisplayName("updateUser - 일반회원이 자신의 정보를 수정하면, 회원을 수정하고 그 결과를 리턴한다.")
    @Test
    void updateUser() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";

        var testUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid")
                .name("testName")
                .isAdmin(false)
                .build();

        var requestDto = UserUpdateRequestControllerDto.builder()
                .name("testName2")
                .build();

        var exceptResult = UserResponseDto.builder()
                .uid("thisIsTestUid")
                .name("testName2")
                .isAdmin(false)
                .build();


        doReturn(testUserResponseDto).when(firebaseService).findUserByToken(testToken);
        doReturn(exceptResult).when(userService).updateUser(UserUpdateRequestServiceDto.builder()
                .name("testName2")
                .uid("thisIsTestUid")
                .build());
        //when
        var url = "http://localhost:" + port + "/api/v1/user/thisIsTestUid";
        var result = mvc.perform(
                        put(url)
                                .header("Authorization", "Bearer " + testToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(requestDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("thisIsTestUid"))
                .andExpect(jsonPath("$.name").value("testName2"))
                .andExpect(jsonPath("$.admin").value(false));

        verify(firebaseService).findUserByToken(testToken);
        verify(userService).updateUser(UserUpdateRequestServiceDto.builder()
                .name("testName2")
                .uid("thisIsTestUid")
                .build());
    }

    @DisplayName("updateUser - 관리자 회원이 자신의 정보를 수정하면, 회원을 수정하고 그 결과를 리턴한다.")
    @Test
    void updateUser2() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";

        var testUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid")
                .name("testName")
                .isAdmin(true)
                .build();

        var requestDto = UserUpdateRequestControllerDto.builder()
                .name("testName2")
                .build();

        var exceptResult = UserResponseDto.builder()
                .uid("thisIsTestUid")
                .name("testName2")
                .isAdmin(true)
                .build();


        doReturn(testUserResponseDto).when(firebaseService).findUserByToken(testToken);
        doReturn(exceptResult).when(userService).updateUser(UserUpdateRequestServiceDto.builder()
                .name("testName2")
                .uid("thisIsTestUid")
                .build());
        //when
        var url = "http://localhost:" + port + "/api/v1/user/thisIsTestUid";
        var result = mvc.perform(
                        put(url)
                                .header("Authorization", "Bearer " + testToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(requestDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("thisIsTestUid"))
                .andExpect(jsonPath("$.name").value("testName2"))
                .andExpect(jsonPath("$.admin").value(true));

        verify(firebaseService).findUserByToken(testToken);
        verify(userService).updateUser(UserUpdateRequestServiceDto.builder()
                .name("testName2")
                .uid("thisIsTestUid")
                .build());
    }

    @DisplayName("updateUser - 관리자 회원이 타인의 정보를 수정하면, 회원을 수정하고 그 결과를 리턴한다.")
    @Test
    void updateUser3() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";

        var testUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid2")
                .name("testName123")
                .isAdmin(true)
                .build();

        var requestDto = UserUpdateRequestControllerDto.builder()
                .name("testName2")
                .build();

        var exceptResult = UserResponseDto.builder()
                .uid("thisIsTestUid")
                .name("testName2")
                .isAdmin(false)
                .build();


        doReturn(testUserResponseDto).when(firebaseService).findUserByToken(testToken);
        doReturn(exceptResult).when(userService).updateUser(UserUpdateRequestServiceDto.builder()
                .name("testName2")
                .uid("thisIsTestUid")
                .build());
        //when
        var url = "http://localhost:" + port + "/api/v1/user/thisIsTestUid";
        var result = mvc.perform(
                        put(url)
                                .header("Authorization", "Bearer " + testToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(requestDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("thisIsTestUid"))
                .andExpect(jsonPath("$.name").value("testName2"))
                .andExpect(jsonPath("$.admin").value(false));

        verify(firebaseService).findUserByToken(testToken);
        verify(userService).updateUser(UserUpdateRequestServiceDto.builder()
                .name("testName2")
                .uid("thisIsTestUid")
                .build());
    }

    @DisplayName("updateUser - 일반 회원이 타인의 정보를 수정하면, 403을 리턴한다.")
    @Test
    void updateUser4() throws Exception {
        //given
        var testToken = "thisIsTestIdToken";

        var testUserResponseDto = UserResponseDto.builder()
                .uid("thisIsTestUid2")
                .name("testName123")
                .isAdmin(false)
                .build();

        var requestDto = UserUpdateRequestControllerDto.builder()
                .name("testName2")
                .build();


        doReturn(testUserResponseDto).when(firebaseService).findUserByToken(testToken);

        //when
        var url = "http://localhost:" + port + "/api/v1/user/thisIsTestUid";
        var result = mvc.perform(
                        put(url)
                                .header("Authorization", "Bearer " + testToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(requestDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        result.andExpect(status().isForbidden());

        verify(firebaseService).findUserByToken(testToken);
    }
}