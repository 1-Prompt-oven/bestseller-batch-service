package com.promptoven.sellerbatchservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "seller_aggregate", indexes = {
        @Index(name = "idx_seller_aggregate_member_uuid", columnList = "memberUuid"),
        @Index(name = "idx_seller_aggregate_ranking", columnList = "ranking ASC")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Comment("리뷰 평균")
    private Double reviewAvg;

    @Builder
    public AggregateEntity(Long id, String memberUuid, Long sellsCount, Long ranking, Double reviewAvg) {
        this.id = id;
        this.memberUuid = memberUuid;
        this.sellsCount = sellsCount;
        this.ranking = ranking;
        this.reviewAvg = reviewAvg;
    }

    public void updateSellsCount(Long sellsCount) {
        this.sellsCount = sellsCount;
    }

    public void updateRank(Long ranking) {
        this.ranking = ranking;
    }

    public void updateReviewAvg(Double reviewAvg) {
        this.reviewAvg = reviewAvg;
    }
}
