package com.promptoven.sellerbatchservice.presentation;

import com.promptoven.sellerbatchservice.application.aggregate.AggregateService;
import com.promptoven.sellerbatchservice.application.batch.BatchSchedule;
import com.promptoven.sellerbatchservice.dto.in.AggregatePagingRequestDto;
import com.promptoven.sellerbatchservice.dto.out.AggregateResponseDto;
import com.promptoven.sellerbatchservice.global.common.CursorPage;
import com.promptoven.sellerbatchservice.global.common.response.BaseResponse;
import com.promptoven.sellerbatchservice.vo.out.AggregateResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/seller/aggregate")
@RequiredArgsConstructor
public class AggregateController {

    private final AggregateService aggregateService;
    private final BatchSchedule batchSchedule;

    @GetMapping("/testSchedule")
    public String batch() throws Exception {
        batchSchedule.scheduleBatch();
        return "OK";
    }

    @GetMapping({"/{memberUuid}"})
    public BaseResponse<AggregateResponseVo> getAggregateData(@PathVariable String memberUuid) {

        AggregateResponseDto aggregateResponseDto = aggregateService.getAggregateData(memberUuid);

        return new BaseResponse<>(AggregateResponseDto.toVo(aggregateResponseDto));
    }

    @GetMapping("/bestSellers")
    public CursorPage<AggregateResponseVo> getBestSellersByRanking(
            @RequestParam(required = false) Integer lastRanking,
            @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        AggregatePagingRequestDto requestDto = AggregatePagingRequestDto.builder()
                .lastRanking(lastRanking)
                .pageSize(pageSize)
                .build();

        CursorPage<AggregateResponseDto> responsePage = aggregateService.getBestSellersByRanking(requestDto);

        List<AggregateResponseVo> content = responsePage.getContent().stream()
                .map(AggregateResponseDto::toVo)
                .toList();

        return new CursorPage<>(content, responsePage.getNextCursor(), responsePage.getHasNext(),
                responsePage.getPageSize(), responsePage.getPage());
    }
}
