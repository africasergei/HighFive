package com.jobPrize.companyService.dto;

import java.util.List;

import com.jobPrize.entity.memToCom.EducationLevel;
import com.jobPrize.entity.memToCom.Similarity;
import com.jobPrize.entity.member.Education;
import com.jobPrize.entity.member.Member;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberPoolDto {
    private Long memberId;
    private String name;
    private String job;
    private boolean hasCareer;
    private int score;
    private Long jobPostingId;
    private EducationLevel educationLevel; 

    public static MemberPoolDto from(Member member, List<Education> educations, Similarity similarity  ) {
        return MemberPoolDto.builder()
                .memberId(member.getUser().getId())
                .name(member.getUser().getName())
                .job(member.getCareers().isEmpty() ? null : member.getCareers().get(0).getJob()) 
                .hasCareer(!member.getCareers().isEmpty())
                .educationLevel(educations.isEmpty() ? null : educations.get(0).getEducationLevel())
                .score(similarity.getScore())
                .build();
    }
}


