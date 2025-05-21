package com.jobPrize.entity.company;

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
@Getter
@Entity
@Table(name = "job_posting_image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostingImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_posting_image_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_posting_id", nullable = false)
	private JobPosting jobPosting;

	@Column(name = "image_url", nullable = false)
	private String imageUrl;
	
	public void updateImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public JobPostingImage(JobPosting jobPosting, String imageUrl) {
	    this.jobPosting = jobPosting;
	    this.imageUrl = imageUrl;
	}



}