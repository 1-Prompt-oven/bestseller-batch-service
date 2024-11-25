package com.promptoven.sellerbatchservice.dto.in;

import com.promptoven.sellerbatchservice.domain.EventType;
import com.promptoven.sellerbatchservice.domain.SellerBatchEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessageDto {

    private Map<String, String> productSellerMap; // key: productUuid, value: memberUuid

    public List<SellerBatchEntity> toEntities(EventType type) {
        return productSellerMap.values().stream()
                .map(memberUuid -> SellerBatchEntity.builder()
                        .memberUuid(memberUuid)
                        .type(type)
                        .build())
                .collect(Collectors.toList());
    }
}
