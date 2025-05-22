package com.jobPrize.companyService.service.proposal.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.jobPrize.companyService.dto.proposal.ProposalResponseListDto;

public interface ProposalSearchService {
    Page<ProposalResponseListDto> readProposalsByCompanyId(Long companyId, Pageable pageable);
}