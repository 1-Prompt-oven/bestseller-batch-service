package com.promptoven.sellerbatchservice.vo.out;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class AggregateResponseVo {

    private final String memberUuid;

    private final Long sellsCount;

    private final Long ranking;

    private final Double reviewAvg;

    private final LocalDate date;

    private final Long rankingChange;

    private final Long dailySellsCount;
}
