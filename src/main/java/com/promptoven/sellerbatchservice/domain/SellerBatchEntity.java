package com.promptoven.sellerbatchservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "seller_batch")
public class SellerBatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchId;

    @Comment("판매자 UUID")
    @Column(nullable = false, length = 50)
    private String memberUuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    @Builder
    public SellerBatchEntity(Long batchId, String memberUuid, EventType type) {
        this.batchId = batchId;
        this.memberUuid = memberUuid;
        this.type = type;
    }
}
