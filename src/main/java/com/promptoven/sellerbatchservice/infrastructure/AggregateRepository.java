package com.promptoven.sellerbatchservice.infrastructure;

import com.promptoven.sellerbatchservice.domain.AggregateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AggregateRepository extends JpaRepository<AggregateEntity, Long>, AggregateRepositoryCustom {

    List<AggregateEntity> findAllByMemberUuidIn(List<String> memberUuids);

    Optional<AggregateEntity> findByMemberUuid(String memberUuid);
}
