package com.jobPrize.entity.common;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscription")			// 구독 테이블
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Subscription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SUBSCRIBE_ID", nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
	
    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate;	// 구독 시작일
	
    @Column(name = "END_DATE", nullable = false)
    private LocalDate endDate;	//	구독 종료일

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    
    public boolean isAtive() {
    	return LocalDate.now().isAfter(startDate) && LocalDate.now().isBefore(endDate);
    }
}
