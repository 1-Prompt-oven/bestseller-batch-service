package com.promptoven.sellerbatchservice.dto.out;

import com.promptoven.sellerbatchservice.domain.AggregateEntity;
import com.promptoven.sellerbatchservice.vo.out.AggregateResponseVo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class AggregateResponseDto {

    private String memberUuid;

    private Long sellsCount;

    private Long ranking;

    private Double reviewAvg;

    private LocalDate date;

    private Long rankingChange;

    private Long dailySellsCount;

    public static AggregateResponseDto toDto(AggregateEntity aggregateEntity) {
        return AggregateResponseDto.builder()
                .memberUuid(aggregateEntity.getMemberUuid())
                .sellsCount(aggregateEntity.getSellsCount())
                .ranking(aggregateEntity.getRanking())
                .reviewAvg(aggregateEntity.getReviewAvg())
                .date(aggregateEntity.getDate())
                .rankingChange(aggregateEntity.getRankingChange())
                .dailySellsCount(aggregateEntity.getDailySellsCount())
                .build();
    }

    public static AggregateResponseVo toVo(AggregateResponseDto aggregateResponseDto) {
        return AggregateResponseVo.builder()
                .memberUuid(aggregateResponseDto.getMemberUuid())
                .sellsCount(aggregateResponseDto.getSellsCount())
                .ranking(aggregateResponseDto.getRanking())
                .reviewAvg(aggregateResponseDto.getReviewAvg())
                .date(aggregateResponseDto.getDate())
                .rankingChange(aggregateResponseDto.getRankingChange())
                .dailySellsCount(aggregateResponseDto.getDailySellsCount())
                .build();
    }
}
