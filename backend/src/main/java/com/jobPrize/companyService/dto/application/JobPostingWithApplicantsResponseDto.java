package com.jobPrize.companyService.dto.application;

import java.time.LocalDate;
import java.util.List;

import com.jobPrize.entity.company.CompanyType;
import com.jobPrize.entity.memToCom.EducationLevel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobPostingWithApplicantsResponseDto {
	private final Long id;
	private CompanyType companyType;
	private final String title;
	private final String job;
	private final String worklocation;
	private final String careerType;
	private EducationLevel ediEducationLevel;
	private final LocalDate createdDate;
	private final List<ApplicationResponseDto> applications;
}
