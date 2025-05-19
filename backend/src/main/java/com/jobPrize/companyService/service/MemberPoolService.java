package com.jobPrize.companyService.service;

import java.util.List;

import com.jobPrize.companyService.dto.memberPool.MemberPoolListResponseDto;
import com.jobPrize.entity.memToCom.Similarity;
import com.jobPrize.entity.member.Education;
import com.jobPrize.entity.member.Member;

public class MemberPoolService {
	public static MemberPoolListResponseDto of(Member member, List<Education> educations, Similarity similarity  ) {
	    return MemberPoolListResponseDto.builder()
	            .id(member.getUser().getId())
	            .name(member.getUser().getName())
	            .job(member.getCareers().isEmpty() ? null : member.getCareers().get(0).getJob()) 
	            .hasCareer(!member.getCareers().isEmpty())
	            .educationLevel(educations.isEmpty() ? null : educations.get(0).getEducationLevel())
	            .jobPostingId(similarity.getJobPosting().getId())
	            .score(similarity.getScore())
	            .build();
	}
}
