package com.promptoven.sellerbatchservice.presentation;

import com.promptoven.sellerbatchservice.application.aggregate.AggregateService;
import com.promptoven.sellerbatchservice.application.batch.BatchSchedule;
import com.promptoven.sellerbatchservice.dto.in.AggregatePagingRequestDto;
import com.promptoven.sellerbatchservice.dto.out.AggregateResponseDto;
import com.promptoven.sellerbatchservice.global.common.CursorPage;
import com.promptoven.sellerbatchservice.global.common.response.BaseResponse;
import com.promptoven.sellerbatchservice.vo.out.AggregateResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/seller-batch/aggregate")
@RequiredArgsConstructor
public class AggregateController {

    private final AggregateService aggregateService;
    private final BatchSchedule batchSchedule;

    @Operation(summary = "배치 테스트", description = "배치 테스트")
    @GetMapping("/testSchedule")
    public String batch() throws Exception {
        batchSchedule.scheduleBatch();
        return "OK";
    }

    @Operation(summary = "판매자별 집계 데이터 조회", description = "판매자별 집계 데이터 조회")
    @GetMapping({"/{memberUuid}"})
    public BaseResponse<AggregateResponseVo> getAggregateData(
            @PathVariable String memberUuid,
            @RequestParam LocalDate date) {

        AggregateResponseDto aggregateResponseDto = aggregateService.getAggregateData(memberUuid, date);

        return new BaseResponse<>(AggregateResponseDto.toVo(aggregateResponseDto));
    }

    @Operation(summary = "베스트셀러 조회", description = "베스트셀러 조회")
    @GetMapping("/bestSellers")
    public BaseResponse<CursorPage<AggregateResponseVo>> getBestSellersByRanking(
            @RequestParam(required = false) Integer lastRanking,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam LocalDate date) {
        AggregatePagingRequestDto requestDto = AggregatePagingRequestDto.builder()
                .lastRanking(lastRanking)
                .pageSize(pageSize)
                .date(date)
                .build();

        CursorPage<AggregateResponseDto> responsePage = aggregateService.getBestSellersByRanking(requestDto);

        List<AggregateResponseVo> content = responsePage.getContent().stream()
                .map(AggregateResponseDto::toVo)
                .toList();

        return new BaseResponse<>(new CursorPage<>(content, responsePage.getNextCursor(), responsePage.getHasNext(),
                responsePage.getPageSize(), responsePage.getPage()));
    }
}
