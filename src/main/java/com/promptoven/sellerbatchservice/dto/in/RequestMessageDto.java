package com.promptoven.sellerbatchservice.dto.in;

import com.promptoven.sellerbatchservice.domain.EventType;
import com.promptoven.sellerbatchservice.domain.SellerBatchEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessageDto {

    private Long paymentId;

    private String memberUuid;

    private List<String> productUuids;

    public SellerBatchEntity toEntity(EventType type) {
        return SellerBatchEntity.builder()
                .memberUuid(memberUuid)
                .type(type)
                .build();
    }
}
