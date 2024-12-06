package com.promptoven.sellerbatchservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "seller_aggregate", indexes = {
        @Index(name = "idx_seller_aggregate_member_date", columnList = "memberUuid, date"),
        @Index(name = "idx_seller_aggregate_date_ranking", columnList = "date, ranking ASC"),
        @Index(name = "idx_seller_aggregate_member", columnList = "memberUuid")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AggregateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("판매자 UUID")
    @Column(nullable = false, length = 50)
    private String memberUuid;

    @Comment("총 판매 수량")
    @Column(nullable = false)
    private Long sellsCount;

    @Comment("판매자 순위")
    private Long ranking;

    @Comment("리뷰 평균")
    private Double reviewAvg;

    @Comment("집계 날짜")
    @Column(nullable = false)
    private LocalDate date;

    @Comment("랭킹 변동 수")
    private Long rankingChange;

    @Comment("일 별 판매량")
    private Long dailySellsCount;

    @Builder
    public AggregateEntity(Long id, String memberUuid, Long sellsCount, Long ranking, Double reviewAvg, LocalDate date, Long rankingChange, Long dailySellsCount) {
        this.id = id;
        this.memberUuid = memberUuid;
        this.sellsCount = sellsCount;
        this.ranking = ranking;
        this.reviewAvg = reviewAvg;
        this.date = date;
        this.rankingChange = rankingChange;
        this.dailySellsCount = dailySellsCount;
    }

    public void updateSellsCount(Long sellsCount) {
        this.sellsCount = sellsCount;
    }

    public void updateRank(Long ranking) {
        this.ranking = ranking;
    }

    public void updateRankingChange(Long rankingChange) {
        this.rankingChange = rankingChange;
    }

    public void updateReviewAvg(Double reviewAvg) {
        this.reviewAvg = reviewAvg;
    }

    public void setDailySellsCount(Long dailySellsCount) {
        this.dailySellsCount = dailySellsCount;
    }
}
