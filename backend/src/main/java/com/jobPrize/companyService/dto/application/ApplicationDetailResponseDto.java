package com.jobPrize.companyService.dto.application;

import com.jobPrize.entity.common.User;
import com.jobPrize.entity.member.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationDetailResponseDto {
	private final Long id;
	private final String name;
	private final String email;
	private Gender gender;
	private final int age;
	private final boolean hasCareer;
	private final String job;
	private final String phone;
	private final boolean isInterested;
	private String resumeJson;
	private String careerDescriptionJson;
	private String coverLetterJson;
	private String applicationStatus;
}
