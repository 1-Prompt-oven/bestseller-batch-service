package com.promptoven.sellerbatchservice.infrastructure;

import com.promptoven.sellerbatchservice.domain.AggregateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AggregateRepository extends JpaRepository<AggregateEntity, Long>, AggregateRepositoryCustom {

    List<AggregateEntity> findAllByMemberUuidIn(List<String> memberUuids);

    Optional<AggregateEntity> findByMemberUuid(String memberUuid);

    // 특정 날짜에 해당하는 모든 집계 데이터를 조회
    List<AggregateEntity> findAllByDate(LocalDate date);

    // 판매자와 날짜로 특정 데이터 조회
    Optional<AggregateEntity> findByMemberUuidAndDate(String memberUuid, LocalDate date);

    // 특정 날짜 이전 데이터 조회
    List<AggregateEntity> findAllByDateBefore(LocalDate date);

    // 날짜 범위 조회
    List<AggregateEntity> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM AggregateEntity a WHERE a.date = (SELECT MAX(b.date) FROM AggregateEntity b WHERE b.memberUuid = a.memberUuid)")
    List<AggregateEntity> findLatestDataForAllMembers();
}
