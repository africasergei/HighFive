package com.jobPrize.companyService.dto.application;

import java.time.LocalDate;

import com.jobPrize.entity.memToCom.EducationLevel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationListResponseDto {
	private final Long memberId;
	private final Long jobPostingId;
	private final Long companyId;
	private final String name;
	private Gender gender;
	private int age;
	private final boolean hasCareer;
	private final String job;
	private EducationLevel educationLevel;
	private final LocalDate createdDate;
	private final boolean isInterested;
	}
