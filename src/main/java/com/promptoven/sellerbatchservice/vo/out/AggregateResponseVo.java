package com.promptoven.sellerbatchservice.vo.out;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AggregateResponseVo {

    private final String memberUuid;

    private final Long sellsCount;

    private final Long ranking;
}
