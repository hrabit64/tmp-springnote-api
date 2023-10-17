package com.springnote.api.service;

import com.springnote.api.domain.jpa.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

}
