package com.jobPrize.companyService.dto.memberPool;

import com.jobPrize.entity.memToCom.EducationLevel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberFilterCondition {

	private final boolean hasCareer;

	private final EducationLevel education;

	private final String address;
	
	private final String job;
	
}