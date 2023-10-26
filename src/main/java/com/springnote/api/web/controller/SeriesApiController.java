package com.springnote.api.web.controller;

import com.springnote.api.dto.series.common.SeriesResponseDto;
import com.springnote.api.dto.series.controller.SeriesAddRequestControllerDto;
import com.springnote.api.dto.series.controller.SeriesUpdateRequestControllerDto;
import com.springnote.api.service.SeriesService;
import com.springnote.api.utils.aop.auth.AuthLevel;
import com.springnote.api.utils.aop.auth.EnableAuth;
import com.springnote.api.utils.exception.controller.ControllerErrorCode;
import com.springnote.api.utils.exception.controller.ControllerException;
import com.springnote.api.utils.modelAssembler.SeriesResponseDtoAssembler;
import com.springnote.api.utils.validation.page.PageableSortKeyCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "")
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/series")
public class SeriesApiController {

    private final SeriesService seriesService;
    private final SeriesResponseDtoAssembler assembler;


    private final PagedResourcesAssembler<SeriesResponseDto> pagedResourcesAssembler;

    @GetMapping("")
    public PagedModel<EntityModel<SeriesResponseDto>> getSeries(@PageableDefault(page = 0, size = 100)
                              @PageableSortKeyCheck(sortKeys = {"id", "title", "description"}) Pageable pageable) {
        var result = seriesService.findAll(pageable);
        return pagedResourcesAssembler.toModel(result, assembler);
    }

    @GetMapping("/search")
    public PagedModel<EntityModel<SeriesResponseDto>> getSeriesWithSearch(
            @PageableDefault(page = 0, size = 100)
            @PageableSortKeyCheck(sortKeys = {"id", "title", "description"}) Pageable pageable,
            @RequestParam(required = false,defaultValue = "") String title,
            @RequestParam(required = false,defaultValue = "") String description
    ) {
        isQueryParamValid(title, description);
        var result = seriesService.findWithIndex(pageable, title, description);
        return pagedResourcesAssembler.toModel(result, assembler);
    }

    @GetMapping("/{seriesId}")
    public EntityModel<SeriesResponseDto> getSeriesById(@PathVariable Long seriesId) {
        var result = seriesService.findById(seriesId);
        return assembler.toModel(result);
    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @PostMapping("")
    public EntityModel<SeriesResponseDto> createSeries(@RequestBody @Validated SeriesAddRequestControllerDto dto) {
        var result = seriesService.createSeries(dto.toServiceDto());
        return assembler.toModel(result);
    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @PutMapping("/{seriesId}")
    public EntityModel<SeriesResponseDto> updateSeries(@PathVariable Long seriesId, @RequestBody @Validated SeriesUpdateRequestControllerDto dto) {
        var result = seriesService.updateSeries(dto.toServiceDto(seriesId));
        return assembler.toModel(result);
    }

    @EnableAuth(authLevel = AuthLevel.ADMIN)
    @DeleteMapping("/{seriesId}")
    public EntityModel<SeriesResponseDto> deleteSeries(@PathVariable Long seriesId) {
        var result = seriesService.deleteSeries(seriesId);
        return assembler.toModel(result);
    }

    private void isQueryParamValid(String title, String description) {
        if(title.equals("") && description.equals("")) {
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "검색 옵션이 비워져 있습니다.");
        }
        if(!title.equals("") && title.length() < 2){
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "제목은 2글자 이상이어야 합니다.");
        }
        if(title.length() > 10){
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "제목은 10글자 이하이어야 합니다.");
        }
        if(!description.equals("") && description.length() < 2){
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "설명은 2글자 이상이어야 합니다.");
        }
        if(description.length() > 12){
            throw new ControllerException(ControllerErrorCode.NOT_VALID, "설명은 12글자 이하이어야 합니다.");
        }
    }
}
