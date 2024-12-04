package com.promptoven.sellerbatchservice.application.kafka;

import com.promptoven.sellerbatchservice.domain.AggregateEntity;
import com.promptoven.sellerbatchservice.domain.EventType;
import com.promptoven.sellerbatchservice.domain.SellerBatchEntity;
import com.promptoven.sellerbatchservice.dto.in.RequestAggregateMessageDto;
import com.promptoven.sellerbatchservice.dto.in.RequestMessageDto;
import com.promptoven.sellerbatchservice.infrastructure.AggregateRepository;
import com.promptoven.sellerbatchservice.infrastructure.SellerBatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private static final String GROUP_ID = "kafka-seller-service";
    private final SellerBatchRepository sellerBatchRepository;
    private final AggregateRepository aggregateRepository;

    @KafkaListener(topics = "${payment-create-event}", groupId = GROUP_ID)
    public void consumeCreate(RequestMessageDto message) {

        log.info("consumeCreate: {}", message);

        consumeEvent(message, EventType.CREATE);
    }

    @KafkaListener(topics = "${payment-delete-event}", groupId = GROUP_ID)
    public void consumeDelete(RequestMessageDto message) {

        log.info("consumeDelete: {}", message);

        consumeEvent(message, EventType.DELETE);
    }

    @KafkaListener(topics = "${aggregate-finish-event}", groupId = "kafka-aggregate-service")
    public void consumeAggregateFinish(RequestAggregateMessageDto message) {
        log.info("consumeAggregateFinish: {}", message);

        // Kafka에서 전달받은 sellerAggregateMap 처리
        Map<String, Double> sellerAggregateMap = message.getSellerAggregateMap();

        for (Map.Entry<String, Double> entry : sellerAggregateMap.entrySet()) {
            String sellerUuid = entry.getKey();
            Double reviewAvg = entry.getValue();

            // 데이터베이스에서 판매자 정보 조회
            aggregateRepository.findByMemberUuid(sellerUuid)
                    .ifPresentOrElse(
                            aggregate -> {
                                aggregate.updateReviewAvg(reviewAvg);
                                aggregateRepository.save(aggregate);
                            },
                            () -> {
                                AggregateEntity newAggregate = AggregateEntity.builder()
                                        .memberUuid(sellerUuid)
                                        .sellsCount(0L) // 초기 값
                                        .ranking(null)  // 초기 값
                                        .reviewAvg(reviewAvg)
                                        .build();
                                aggregateRepository.save(newAggregate);
                                log.info("Created new aggregate for sellerUuid {}: {}", sellerUuid, reviewAvg);
                            }
                    );
        }
    }

    private void consumeEvent(RequestMessageDto message, EventType eventType) {

        List<SellerBatchEntity> entities = message.toEntities(eventType);

        sellerBatchRepository.saveAll(entities);
    }
}
