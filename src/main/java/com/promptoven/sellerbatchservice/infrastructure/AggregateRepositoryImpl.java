package com.promptoven.sellerbatchservice.infrastructure;

import com.promptoven.sellerbatchservice.domain.AggregateEntity;
import com.promptoven.sellerbatchservice.domain.QAggregateEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AggregateRepositoryImpl implements AggregateRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<AggregateEntity> findBestSellersByRanking(Integer lastRanking, int pageSize) {
        QAggregateEntity aggregate = QAggregateEntity.aggregateEntity;

        return jpaQueryFactory
                .selectFrom(aggregate)
                .where(lastRanking == null
                        ? null
                        : aggregate.ranking.gt(lastRanking)) // Cursor 조건
                .orderBy(aggregate.ranking.asc()) // 정렬 조건
                .limit(pageSize) // 페이징 처리
                .fetch();
    }
}
