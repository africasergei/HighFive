package com.jobPrize.dto.memToCom.jobPosting;

import java.time.LocalDate;

import com.jobPrize.entity.company.JobPosting;
import com.jobPrize.entity.memToCom.Similarity;
import com.jobPrize.enumerate.CompanyType;
import com.jobPrize.enumerate.EducationLevel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobPostingSummaryForMemberDto {
	
	private Long id;
	
	private String title;
	
	private String companyName;
	
	private CompanyType type;
	
	private String job;
	
	private String workLocation;
	
	private String careerType;
	
	private EducationLevel educationLevel;
	
	private int similarityScore;
	
	private LocalDate createdDate;
	
	public static JobPostingSummaryForMemberDto from(Similarity similarity) {
		JobPosting jobPosting = similarity.getJobPosting();
		
		
		return JobPostingSummaryForMemberDto
				.builder()
				.id(jobPosting.getId())
				.title(jobPosting.getTitle())
				.companyName(jobPosting.getCompany().getCompanyName())
				.job(jobPosting.getJob())
				.type(jobPosting.getCompany().getType())
				.workLocation(jobPosting.getWorkLocation())
				.careerType(jobPosting.getCareerType())
				.educationLevel(jobPosting.getEducationLevel())
				.similarityScore(similarity.getScore())
				.createdDate(jobPosting.getCreatedDate())
				.build();
		
	}
	
}
