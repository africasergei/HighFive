package com.jobPrize.companyService.service.proposal.api;

import com.jobPrize.companyService.dto.proposal.ProposalRequestCreateDto;
import com.jobPrize.entity.memToCom.Proposal;

public interface ProposalActionService {
    Proposal createProposal(Long userId, ProposalRequestCreateDto dto);
}