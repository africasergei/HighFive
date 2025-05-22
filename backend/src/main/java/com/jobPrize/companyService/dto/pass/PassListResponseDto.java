package com.jobPrize.companyService.dto.pass;

import java.time.LocalDate;

import com.jobPrize.entity.common.GenderType;
import com.jobPrize.entity.memToCom.EducationLevel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PassListResponseDto {
	private final Long applicationId;
	private final Long memberId;
	private final String name;
	private final GenderType gender;
	private final LocalDate birthDate;
	private final boolean hasCareer;
	private final String job;
	private final EducationLevel educationLevel;
	private final LocalDate passDate;
	private final String title;
	private final boolean isPassed; 
}
