package com.jobPrize.repository.memToCom.interest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jobPrize.entity.memToCom.Interest;

public interface InterestRepositoryCustom {
	Page<Interest> findAllByCompanyId(Long id, Pageable pageable, boolean isMemberInterest);
}
