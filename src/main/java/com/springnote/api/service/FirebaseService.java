package com.springnote.api.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.springnote.api.dto.firebase.service.FirebaseResponse;
import com.springnote.api.dto.user.common.UserResponseDto;
import com.springnote.api.utils.exception.service.ServiceErrorCode;
import com.springnote.api.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FirebaseService {
    private final FirebaseAuth firebaseAuth;
    private final UserService userService;
    public FirebaseResponse decodeToken(String token) {
        try {
            return FirebaseResponse.builder()
                    .uid(firebaseAuth.verifyIdToken(token).getUid())
                    .build();
        } catch (FirebaseAuthException e){
            throw new ServiceException(ServiceErrorCode.NOT_FOUND,"해당 토큰을 디코딩했으나, 유효한 정보를 찾을 수 없습니다. 유효하지 않거나 만료된 토큰입니다.");
        }
        catch (Exception e) {
            throw new ServiceException(ServiceErrorCode.UNKNOWN_ERROR,"토큰 디코딩에 실패했습니다.");
        }
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUserByToken(String token) {
        var tokenInfo = decodeToken(token);
        return userService.findUserById(tokenInfo.getUid());
    }

    @Transactional
    public int deleteFirebaseUserWithToken(String token){
        //TODO
        return 0;
    }

    @Transactional
    public int deleteFirebaseUserWithUid(String uid){
        //TODO
        return 0;
    }
}
