package com.promptoven.sellerbatchservice.dto.in;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class AggregatePagingRequestDto {

    private Integer lastRanking; // 이전 페이지의 마지막 ranking

    private Integer pageSize; // 한 페이지당 데이터 개수

    private LocalDate date; // 조회 날짜
}
