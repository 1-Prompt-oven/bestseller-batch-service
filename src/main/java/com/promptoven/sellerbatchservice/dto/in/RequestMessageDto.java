package com.promptoven.sellerbatchservice.dto.in;

import com.promptoven.sellerbatchservice.domain.EventType;
import com.promptoven.sellerbatchservice.domain.SellerBatchEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RequestMessageDto {

    private String memberUuid;

    public SellerBatchEntity toEntity(EventType type) {
        return SellerBatchEntity.builder()
                .memberUuid(memberUuid)
                .type(type)
                .build();
    }
}
