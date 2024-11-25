package com.promptoven.sellerbatchservice.application.aggregate;

import com.promptoven.sellerbatchservice.domain.AggregateEntity;
import com.promptoven.sellerbatchservice.dto.out.AggregateResponseDto;
import com.promptoven.sellerbatchservice.infrastructure.AggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AggregateServiceImpl implements AggregateService {

    private final AggregateRepository aggregateRepository;

    @Override
    public AggregateResponseDto getAggregateData(String memberUuid) {

        Optional<AggregateEntity> aggregateEntity = aggregateRepository.findByMemberUuid(memberUuid);

        return aggregateEntity
                .map(AggregateResponseDto::toDto)
                .orElseGet(() -> new AggregateResponseDto(
                        memberUuid, 0L, null
                ));
    }
}
