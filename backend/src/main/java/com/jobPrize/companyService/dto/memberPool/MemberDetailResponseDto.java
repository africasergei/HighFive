package com.jobPrize.companyService.dto.memberPool;

import com.jobPrize.entity.member.Education;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

import com.jobPrize.entity.common.GenderType;
@Getter
@Builder
public class MemberDetailResponseDto {
	private final Long id;
	private final String name;
	private final String email;
	private GenderType gender;
	private final LocalDate birthDate;
	private final boolean hasCareer;
	private final String job;
	private final String phone;

	private final EducationResponseDto educationResponseDto;
	private final CareerResponseDto  careerResponseDto;
	private final CertificationResponseDto certificationResponseDto;
	private final LanguageTestResponseDto languageTestResponseDto;
	private final boolean isInterested;
}
