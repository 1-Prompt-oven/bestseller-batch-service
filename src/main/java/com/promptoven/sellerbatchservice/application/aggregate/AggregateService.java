package com.promptoven.sellerbatchservice.application.aggregate;

import com.promptoven.sellerbatchservice.dto.out.AggregateResponseDto;

public interface AggregateService {

    AggregateResponseDto getAggregateData(String memberUuid);
}
