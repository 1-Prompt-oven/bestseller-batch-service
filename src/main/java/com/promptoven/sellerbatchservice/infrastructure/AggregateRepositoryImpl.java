package com.promptoven.sellerbatchservice.infrastructure;

import com.promptoven.sellerbatchservice.domain.AggregateEntity;
import com.promptoven.sellerbatchservice.domain.QAggregateEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AggregateRepositoryImpl implements AggregateRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<AggregateEntity> findBestSellersByRankingAndDate(Integer lastRanking, int pageSize, LocalDate date) {
        QAggregateEntity aggregate = QAggregateEntity.aggregateEntity;

        return jpaQueryFactory
                .selectFrom(aggregate)
                .where(
                        aggregate.date.eq(date)
                                .and(lastRanking == null ? null : aggregate.ranking.gt(lastRanking))
                )
                .orderBy(aggregate.ranking.asc())
                .limit(pageSize)
                .fetch();
    }
}
