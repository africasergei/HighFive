package com.jobPrize.companyService.dto.memberPool;

import com.jobPrize.entity.memToCom.EducationLevel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class MemberPoolListResponseDto {
	private final Long memberId;
	private final Long companyId;
	private final String name;
	private final String job;
	private final boolean hasCareer;
	private final int score;
	private EducationLevel educationLevel;
	private final Long jobPostingId;
	private final boolean isInterested;

}