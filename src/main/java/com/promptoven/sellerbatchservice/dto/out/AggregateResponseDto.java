package com.promptoven.sellerbatchservice.dto.out;

import com.promptoven.sellerbatchservice.domain.AggregateEntity;
import com.promptoven.sellerbatchservice.vo.out.AggregateResponseVo;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AggregateResponseDto {

    private String memberUuid;

    private Long sellsCount;

    private Long ranking;

    private Double reviewAvg;

    public static AggregateResponseDto toDto(AggregateEntity aggregateEntity) {
        return AggregateResponseDto.builder()
                .memberUuid(aggregateEntity.getMemberUuid())
                .sellsCount(aggregateEntity.getSellsCount())
                .ranking(aggregateEntity.getRanking())
                .reviewAvg(aggregateEntity.getReviewAvg())
                .build();
    }

    public static AggregateResponseVo toVo(AggregateResponseDto aggregateResponseDto) {
        return AggregateResponseVo.builder()
                .memberUuid(aggregateResponseDto.getMemberUuid())
                .sellsCount(aggregateResponseDto.getSellsCount())
                .ranking(aggregateResponseDto.getRanking())
                .reviewAvg(aggregateResponseDto.getReviewAvg())
                .build();
    }
}
