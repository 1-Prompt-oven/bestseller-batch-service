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
@Table(name = "seller_aggregate")
public class AggregateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("판매자 UUID")
    @Column(nullable = false, length = 50)
    private String memberUuid;

    @Comment("판매 수량")
    @Column(nullable = false)
    private Long sellsCount;

    @Comment("판매자 순위")
    private Long ranking;

    @Builder
    public AggregateEntity(Long id, String memberUuid, Long sellsCount, Long ranking) {
        this.id = id;
        this.memberUuid = memberUuid;
        this.sellsCount = sellsCount;
        this.ranking = ranking;
    }

    public void updateSellsCount(Long sellsCount) {
        this.sellsCount = sellsCount;
    }

    public void updateRank(Long ranking) {
        this.ranking = ranking;
    }
}
