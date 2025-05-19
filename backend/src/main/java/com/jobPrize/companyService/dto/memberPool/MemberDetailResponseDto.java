package com.jobPrize.companyService.dto.memberPool;

import com.jobPrize.companyService.dto.application.CareerResponseDto;
import com.jobPrize.companyService.dto.application.CertificationResponseDto;
import com.jobPrize.companyService.dto.application.EducationResponseDto;
import com.jobPrize.companyService.dto.application.LanguageTestResponseDto;
import com.jobPrize.entity.common.User;
import com.jobPrize.entity.member.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDetailResponseDto {
	private final Long id;
	private final String name;
	private final String email;
	private final String gender;
	private final int age;
	private final boolean hasCareer;
	private final String job;
	private final String phone;

	private final EducationResponseDto education;
	private final CareerResponseDto career;
	private final CertificationResponseDto certification;
	private final LanguageTestResponseDto languageTest;
	private final boolean isInterested;
}
