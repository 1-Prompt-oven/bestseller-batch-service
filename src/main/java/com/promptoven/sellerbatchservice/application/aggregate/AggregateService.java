package com.promptoven.sellerbatchservice.application.aggregate;

import com.promptoven.sellerbatchservice.dto.in.AggregatePagingRequestDto;
import com.promptoven.sellerbatchservice.dto.out.AggregateResponseDto;
import com.promptoven.sellerbatchservice.global.common.CursorPage;

public interface AggregateService {

    AggregateResponseDto getAggregateData(String memberUuid);

    CursorPage<AggregateResponseDto> getBestSellersByRanking(AggregatePagingRequestDto requestDto);
}
