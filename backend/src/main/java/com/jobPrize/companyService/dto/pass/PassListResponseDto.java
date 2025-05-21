package com.jobPrize.companyService.dto.pass;

import java.time.LocalDate;

import com.jobPrize.entity.memToCom.EducationLevel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PassListResponseDto {
	private final Long applicantionId;
	private final Long memberId;
	private final String name;
	private Gender gender;
	private final int age;
	private final boolean hasCareer;
	private final String job;
	private EducationLevel educationLevel;
	private final LocalDate createDate;
	private final String title;
	private final boolean isInterested;
}
