package com.jobPrize.companyService.dto.jobPosting;

import java.time.LocalDate;

import com.jobPrize.entity.company.CompanyType;
import com.jobPrize.entity.memToCom.EducationLevel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobPostingListResponseDto {
private final Long id;
private CompanyType companyType;
private final String title;
private final String job;
private final String workLocation;
private final String careerType;
private EducationLevel educationLevel;
private final LocalDate createdDate;

}
