package com.jobPrize.repository.memToCom.pass;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jobPrize.entity.memToCom.Pass;

public interface PassRepositoryCustom {
    Page<Pass> findPassedApplicantsByCompanyId(Long companyId, Pageable pageable);
}