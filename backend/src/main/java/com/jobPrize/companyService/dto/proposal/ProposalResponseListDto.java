package com.jobPrize.companyService.dto.proposal;

import java.time.LocalDate;

import com.jobPrize.entity.memToCom.EducationLevel;
import com.jobPrize.entity.memToCom.ProposalStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProposalResponseListDto {

    private final Long id;
    private final String name;
    private final Gender gender;
    private final int age;
    private final boolean hasCareer;
    private final String job;
    private final EducationLevel educationLevel;
    private final LocalDate proposalDate;
    private final ProposalStatus proposalStatus;
}