package com.jobPrize.repository.memToCom.proposal;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jobPrize.entity.memToCom.Proposal;

public interface ProposalRepositoryCustom {
	Page<Proposal> findAllByCompanyId(Long id, Pageable pageable);
	Page<Proposal> findAllByMemberId(Long id, Pageable pageable);

}
