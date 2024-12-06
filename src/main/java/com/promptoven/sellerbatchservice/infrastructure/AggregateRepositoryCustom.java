package com.promptoven.sellerbatchservice.infrastructure;

import com.promptoven.sellerbatchservice.domain.AggregateEntity;

import java.time.LocalDate;
import java.util.List;

public interface AggregateRepositoryCustom {

    List<AggregateEntity> findBestSellersByRankingAndDate(Integer lastRanking, int pageSize, LocalDate date);
}
