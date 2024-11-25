package com.promptoven.sellerbatchservice.application.kafka;

import com.promptoven.sellerbatchservice.domain.EventType;
import com.promptoven.sellerbatchservice.domain.SellerBatchEntity;
import com.promptoven.sellerbatchservice.dto.in.RequestMessageDto;
import com.promptoven.sellerbatchservice.infrastructure.SellerBatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    //    private static final String CREATE_TOPIC = "create_seller_event"; // 각각 이벤트의 발행
    //    private static final String DELETE_TOPIC = "delete_seller_event";
    private static final String GROUP_ID = "kafka-seller-service";
    private final SellerBatchRepository sellerBatchRepository;

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

    private void consumeEvent(RequestMessageDto message, EventType eventType) {

        List<SellerBatchEntity> entities = message.toEntities(eventType);

        sellerBatchRepository.saveAll(entities);
    }
}
