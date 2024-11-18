package com.promptoven.sellerbatchservice.application.batch;

import com.promptoven.sellerbatchservice.domain.AggregateEntity;
import com.promptoven.sellerbatchservice.domain.EventType;
import com.promptoven.sellerbatchservice.dto.out.AggregateDto;
import com.promptoven.sellerbatchservice.infrastructure.AggregateRepository;
import com.promptoven.sellerbatchservice.infrastructure.SellerBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class BatchService {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final AggregateRepository aggregateRepository;
    private final SellerBatchRepository sellerBatchRepository;

    @Bean
    public Job aggregateJob() {
        return new JobBuilder("aggregateJob", jobRepository)
                .start(createStep())
                .next(deleteStep())
                .build();
    }

    @Bean
    public Step createStep() {
        return new StepBuilder("createStep", jobRepository)
                .tasklet(createTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step deleteStep() {
        return new StepBuilder("deleteStep", jobRepository)
                .tasklet(deleteTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet createTasklet() {
        return (contribution, chunkContext) -> processTasklet(EventType.CREATE);
    }

    @Bean
    public Tasklet deleteTasklet() {
        return (contribution, chunkContext) -> processTasklet(EventType.DELETE);
    }

    private RepeatStatus processTasklet(EventType eventType) {

        List<AggregateDto> aggregateDtoList = sellerBatchRepository.findAggregatesByEventType(eventType);

        Map<String, AggregateEntity> existingEntities = aggregateRepository.findAllByMemberUuidIn(
                aggregateDtoList.stream()
                        .map(AggregateDto::getMemberUuid)
                        .collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(AggregateEntity::getMemberUuid, entity -> entity));

        List<AggregateEntity> updatedEntities = aggregateDtoList.stream()
                .map(aggregateDto -> {
                    AggregateEntity existingEntity = existingEntities.get(aggregateDto.getMemberUuid());

                    if (eventType == EventType.CREATE) {
                        if (existingEntity != null) {
                            existingEntity.updateSellsCount(existingEntity.getSellsCount() + aggregateDto.getCount());
                            return existingEntity;
                        } else {
                            return aggregateDto.toEntity(aggregateDto);
                        }
                    } else if (eventType == EventType.DELETE) {
                        if (existingEntity != null) {
                            long updatedCount = existingEntity.getSellsCount() - aggregateDto.getCount();
                            if (updatedCount > 0) {
                                existingEntity.updateSellsCount(updatedCount);
                                return existingEntity;
                            } else {
                                aggregateRepository.delete(existingEntity);
                                return null;
                            }
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        aggregateRepository.saveAll(updatedEntities);

        return RepeatStatus.FINISHED;
    }
}