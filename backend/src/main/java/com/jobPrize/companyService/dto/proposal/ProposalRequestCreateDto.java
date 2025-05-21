package com.jobPrize.companyService.dto.proposal;

import com.jobPrize.entity.memToCom.Proposal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProposalRequestCreateDto {

	private Long id; 

	@NotEmpty(message = "제목을 입력해야 합니다.")
	@Size(max = 50, message = "제목은 최대 50자까지 입력할 수 있습니다.")
	private String proposalTitle;

	@NotEmpty(message = "회사명을 입력해야 합니다.")
	private String companyName;

	@NotEmpty(message = "제안 내용을 입력해야 합니다.")
	@Size(max = 500, message = "제안 내용은 최대 500자까지 입력할 수 있습니다.")
	private String proposalContent;

	@NotEmpty(message = "직무를 입력해야 합니다.")
	@Size(max = 20, message = "제안 직무는 최대 20자까지 입력할 수 있습니다.")
	private String proposalJob;

	@Min(value = 0, message = "급여는 0 이상이어야 합니다.")
	@Max(value = 100000000, message = "급여는 너무 클 수 없습니다.")
	private int proposalSalary;
	
	
}