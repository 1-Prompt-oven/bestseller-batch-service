package com.promptoven.sellerbatchservice.vo.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AggregateResponseVo {

    private String memberUuid;

    private Long sellsCount;
}
