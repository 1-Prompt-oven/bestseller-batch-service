package com.promptoven.sellerbatchservice.application.aggregate;

import com.promptoven.sellerbatchservice.domain.AggregateEntity;
import com.promptoven.sellerbatchservice.dto.in.AggregatePagingRequestDto;
import com.promptoven.sellerbatchservice.dto.out.AggregateResponseDto;
import com.promptoven.sellerbatchservice.global.common.CursorPage;
import com.promptoven.sellerbatchservice.infrastructure.AggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AggregateServiceImpl implements AggregateService {

    private final AggregateRepository aggregateRepository;

    @Transactional(readOnly = true)
    @Override
    public AggregateResponseDto getAggregateData(String memberUuid) {

        Optional<AggregateEntity> aggregateEntity = aggregateRepository.findByMemberUuid(memberUuid);

        return aggregateEntity
                .map(AggregateResponseDto::toDto)
                .orElseGet(() -> AggregateResponseDto.builder()
                        .memberUuid(memberUuid)
                        .sellsCount(0L)
                        .ranking(null)
                        .build());
    }

    @Transactional(readOnly = true)
    @Override
    public CursorPage<AggregateResponseDto> getBestSellersByRanking(AggregatePagingRequestDto requestDto) {
        List<AggregateEntity> entities = aggregateRepository.findBestSellersByRanking(
                requestDto.getLastRanking(),
                requestDto.getPageSize() + 1
        );

        Long nextCursor = null;
        boolean hasNext = false;

        if (entities.size() > requestDto.getPageSize()) {
            hasNext = true;
            entities = entities.subList(0, requestDto.getPageSize());
            nextCursor = entities.get(requestDto.getPageSize() - 1).getRanking();
        }

        List<AggregateResponseDto> dtos = entities.stream()
                .map(AggregateResponseDto::toDto)
                .collect(Collectors.toList());

        return CursorPage.<AggregateResponseDto>builder()
                .content(dtos)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .pageSize(requestDto.getPageSize())
                .page(dtos.size())
                .build();
    }
}
