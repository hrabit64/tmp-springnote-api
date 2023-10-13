package com.springnote.api.dto.firebase.service;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FirebaseResponse {
    private String uid;
}
