package com.promptoven.sellerbatchservice.presentation;

import com.promptoven.sellerbatchservice.application.aggregate.AggregateService;
import com.promptoven.sellerbatchservice.application.batch.BatchSchedule;
import com.promptoven.sellerbatchservice.dto.out.AggregateResponseDto;
import com.promptoven.sellerbatchservice.global.common.response.BaseResponse;
import com.promptoven.sellerbatchservice.vo.out.AggregateResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
