package com.jobPrize.repository.company.jobPosting;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jobPrize.entity.company.JobPosting;

public interface CompanyJobPostingRepositoryCustom {
	 Page<JobPosting> findAllByCompanyId(Long Id,Pageable pageable); 
	 Optional<JobPosting> findWithJobPostingImageByJobPostingId(Long id);
	 Optional<JobPosting> findLatestJobPosting(Long id);
}
