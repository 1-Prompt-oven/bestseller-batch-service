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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
                .start(processEventStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step processEventStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processEventStep", jobRepository)
                .tasklet(eventTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet eventTasklet() {
        return (contribution, chunkContext) -> {
            processEvent(EventType.CREATE);
            processEvent(EventType.DELETE);
            return RepeatStatus.FINISHED;
        };
    }

    private void processEvent(EventType eventType) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // 모든 판매자의 최신 데이터 가져오기
        Map<String, AggregateEntity> latestData = aggregateRepository.findLatestDataForAllMembers().stream()
                .collect(Collectors.toMap(AggregateEntity::getMemberUuid, entity -> entity));

        // 오늘 이미 저장된 데이터 가져오기
        Map<String, AggregateEntity> todayDataMap = aggregateRepository.findAllByDate(today).stream()
                .collect(Collectors.toMap(AggregateEntity::getMemberUuid, entity -> entity));

        // 이벤트에 따라 데이터 가져오기
        List<AggregateDto> aggregateDtoList = sellerBatchRepository.findAggregatesByEventType(eventType);

        for (AggregateDto aggregateDto : aggregateDtoList) {
            AggregateEntity existingEntity = todayDataMap.get(aggregateDto.getMemberUuid());
            AggregateEntity latestEntity = latestData.get(aggregateDto.getMemberUuid());
            Long dailySellsCount = aggregateDto.getCount();

            if (existingEntity != null) {
                // 이미 오늘 데이터가 존재하면 업데이트
                existingEntity.updateSellsCount(existingEntity.getSellsCount() + dailySellsCount);
                existingEntity.setDailySellsCount(dailySellsCount);
            } else {
                // 오늘 데이터가 없으면 새 데이터 생성
                Long totalSellsCount = dailySellsCount;
                if (latestEntity != null) {
                    totalSellsCount += latestEntity.getSellsCount();
                }

                todayDataMap.put(aggregateDto.getMemberUuid(), AggregateEntity.builder()
                        .memberUuid(aggregateDto.getMemberUuid())
                        .sellsCount(totalSellsCount)
                        .dailySellsCount(dailySellsCount)
                        .reviewAvg(latestEntity != null ? latestEntity.getReviewAvg() : null)
                        .ranking(null) // 이후 랭킹 계산
                        .rankingChange(null) // 이후 랭킹 계산
                        .date(today)
                        .build());
            }
        }

        // 판매 기록이 없는 판매자의 데이터 추가
        for (String memberUuid : latestData.keySet()) {
            if (!todayDataMap.containsKey(memberUuid)) {
                AggregateEntity latestEntity = latestData.get(memberUuid);

                todayDataMap.put(memberUuid, AggregateEntity.builder()
                        .memberUuid(latestEntity.getMemberUuid())
                        .sellsCount(latestEntity.getSellsCount()) // 최신 판매량 유지
                        .dailySellsCount(0L) // 오늘 판매량이 없으면 0으로 초기화
                        .reviewAvg(latestEntity.getReviewAvg())
                        .ranking(null) // 이후 랭킹 계산
                        .rankingChange(null) // 이후 랭킹 계산
                        .date(today)
                        .build());
            }
        }

        // 랭킹 계산
        List<AggregateEntity> sortedTodayData = new ArrayList<>(todayDataMap.values());
        sortedTodayData.sort(Comparator.comparing(AggregateEntity::getSellsCount).reversed());

        int rank = 1;
        for (AggregateEntity entity : sortedTodayData) {
            entity.updateRank((long) rank++);

            // 어제 데이터와 비교하여 랭킹 변동 수 계산
            AggregateEntity yesterdayEntity = aggregateRepository.findByMemberUuidAndDate(
                    entity.getMemberUuid(), yesterday).orElse(null);

            if (yesterdayEntity != null && yesterdayEntity.getRanking() != null) {
                entity.updateRankingChange(yesterdayEntity.getRanking() - entity.getRanking());
            } else {
                entity.updateRankingChange(null); // 신규 데이터는 변동 없음
            }
        }

        // 저장
        aggregateRepository.saveAll(sortedTodayData);
    }
}
