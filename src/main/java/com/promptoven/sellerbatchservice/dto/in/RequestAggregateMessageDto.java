package com.promptoven.sellerbatchservice.dto.in;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class RequestAggregateMessageDto {

    private Map<String, Double> sellerAggregateMap; // key: sellerUuid, value: reviewAvg
}
