package com.jobPrize.entity.company;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

@Getter
@Entity
@Table(name = "advertisement")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Advertisement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "advertisement_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private Company company;

	@Column(name = "image_url", nullable = false)
	private String imageUrl;

	@CreatedDate
	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "is_using_ad", nullable = false) // ✅ 광고 활성 여부 필드 추가
	private boolean isUsingAd;

	public void updateImageUrl(String imageUrl) {
		if (endDate.isBefore(LocalDate.now())) {
			return;
		}
		this.imageUrl = imageUrl;
	}

	public boolean isUsingAd() {
		return LocalDate.now().isAfter(startDate) && LocalDate.now().isBefore(endDate);
	}
}

