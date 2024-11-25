package com.promptoven.sellerbatchservice.infrastructure;

import com.promptoven.sellerbatchservice.domain.AggregateEntity;

import java.util.List;

public interface AggregateRepositoryCustom {
    List<AggregateEntity> findBestSellersByRanking(Integer lastRanking, int pageSize);
}
