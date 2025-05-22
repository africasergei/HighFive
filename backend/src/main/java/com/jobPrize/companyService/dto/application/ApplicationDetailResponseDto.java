package com.jobPrize.companyService.dto.application;

import java.time.LocalDate;

import com.jobPrize.entity.common.GenderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationDetailResponseDto {
	private final Long id;
	private final String name;
	private final String email;
	private final GenderType gender;
	private final LocalDate birthDate;
	private final boolean hasCareer;
	private final String job;
	private final String phone;
	private final boolean isPassed;
	private final boolean isInterested;
	private String resumeJson;
	private String careerDescriptionJson;
	private String coverLetterJson;
}
