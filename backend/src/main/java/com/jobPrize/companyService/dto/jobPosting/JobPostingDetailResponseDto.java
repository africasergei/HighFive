package com.jobPrize.companyService.dto.jobPosting;

import java.util.List;

import com.jobPrize.entity.company.CompanyType;
import com.jobPrize.entity.memToCom.EducationLevel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class JobPostingDetailResponseDto {
	
private final Long companyId;
private final Long jobPostingId;
private final String title;
private CompanyType companyType;
private final String workingHours;
private final String workLocation;
private final String job;
private final String careerType;
private EducationLevel educationLevel;
private final int salary;
private final String content;
private final String requirement;
private final List<String> imageurls;

}
