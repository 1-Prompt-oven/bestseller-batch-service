package com.promptoven.sellerbatchservice.infrastructure;

import com.promptoven.sellerbatchservice.domain.EventType;
import com.promptoven.sellerbatchservice.domain.SellerBatchEntity;
import com.promptoven.sellerbatchservice.dto.out.AggregateDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SellerBatchRepository extends JpaRepository<SellerBatchEntity, Long> {

    @Query("SELECT new com.promptoven.sellerbatchservice.dto.out.AggregateDto(" +
            "s.memberUuid, " +
            "COUNT(s)) " +
            "FROM SellerBatchEntity s " +
            "WHERE s.type = :eventType " +
            "GROUP BY s.memberUuid")
    List<AggregateDto> findAggregatesByEventType(@Param("eventType") EventType eventType);
}
