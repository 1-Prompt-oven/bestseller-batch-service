package com.promptoven.sellerbatchservice.dto.out;

import com.promptoven.sellerbatchservice.domain.AggregateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class AggregateDto {

    private String memberUuid;

    private Long count;


    public AggregateEntity toEntity(AggregateDto aggregateDto) {
        return AggregateEntity.builder()
                .memberUuid(aggregateDto.getMemberUuid())
                .sellsCount(aggregateDto.getCount())
                .build();
    }
}
