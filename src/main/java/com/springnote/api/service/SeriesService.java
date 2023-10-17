package com.springnote.api.service;

import com.springnote.api.domain.elasticsearch.seriesIndex.SeriesIndex;
import com.springnote.api.domain.elasticsearch.seriesIndex.SeriesIndexRepository;
import com.springnote.api.domain.jpa.series.SeriesRepository;
import com.springnote.api.dto.series.common.SeriesResponseDto;
import com.springnote.api.dto.series.service.SeriesAddRequestServiceDto;
import com.springnote.api.dto.series.service.SeriesUpdateRequestServiceDto;
import com.springnote.api.utils.exception.service.ServiceErrorCode;
import com.springnote.api.utils.exception.service.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final SeriesIndexRepository seriesIndexRepository;

    @Transactional(readOnly = true)
    public SeriesResponseDto findById(Long id) {
        return seriesRepository.findById(id)
                .map(SeriesResponseDto::new)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "시리즈를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public Page<SeriesResponseDto> findAll(Pageable pageable){
        return seriesRepository.findAll(pageable).map(SeriesResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<SeriesResponseDto> findWithIndex(Pageable pageable, String title, String description) {
        var searchType = getSearchType(title, description);
        Page<SeriesIndex> result;

        switch (searchType){
            case ALL -> result = seriesIndexRepository.findAllByDescriptionLikeOrTitleLike(description,title,pageable);
            case DESCRIPTION -> result = seriesIndexRepository.findAllByDescriptionLike(description, pageable);
            case TITLE -> result = seriesIndexRepository.findAllByTitleLike(title, pageable);
            default -> throw new ServiceException(ServiceErrorCode.UNKNOWN_ERROR, "잘못된 검색 타입입니다.");
        }

        return result.map(SeriesResponseDto::new);
    }

    @Transactional
    public SeriesResponseDto createSeries(SeriesAddRequestServiceDto dto) {

        //save series
        var newSeries = dto.toEntity();
        checkTitleExist(newSeries.getTitle());

        var savedSeries = seriesRepository.save(newSeries);

        //save index
        var index = savedSeries.toIndex();
        seriesIndexRepository.save(index);

        return new SeriesResponseDto(savedSeries);
    }

    @Transactional
    public SeriesResponseDto updateSeries(SeriesUpdateRequestServiceDto dto) {
        var targetSeries = seriesRepository.findById(dto.getId())
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 시리즈를 찾을 수 없습니다."));

        var updateSeries = dto.toEntity();

        if (!targetSeries.getTitle().equals(updateSeries.getTitle())) checkTitleExist(updateSeries.getTitle());

        //update series
        targetSeries.update(updateSeries);
        var savedSeries = seriesRepository.save(targetSeries);

        //update index
        var updateIndex = savedSeries.toIndex();
        seriesIndexRepository.save(updateIndex);

        return new SeriesResponseDto(savedSeries);
    }

    @Transactional
    public SeriesResponseDto deleteSeries(Long id) {
        var targetSeries = seriesRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceErrorCode.NOT_FOUND, "해당 시리즈를 찾을 수 없습니다."));

        seriesRepository.delete(targetSeries);

        seriesIndexRepository.findById(id).ifPresent(seriesIndexRepository::delete);

        return new SeriesResponseDto(targetSeries);
    }

    @Transactional(readOnly = true)
    public void checkTitleExist(String title) {
        if (seriesRepository.existsByTitle(title)) {
            throw new ServiceException(ServiceErrorCode.ALREADY_EXIST, "이미 존재하는 시리즈 제목입니다.");
        }
    }

    private SearchType getSearchType(String title, String description) {
        if (!title.equals("") && !description.equals("")){
            return SearchType.ALL;
        } else if (!title.equals("")) {
            return SearchType.TITLE;
        } else if (!description.equals("")) {
            return SearchType.DESCRIPTION;
        } else {
            throw new ServiceException(ServiceErrorCode.UNKNOWN_ERROR, "검색 조건이 없습니다.");
        }
    }
    private enum SearchType {
        TITLE, DESCRIPTION, ALL
    }
}
