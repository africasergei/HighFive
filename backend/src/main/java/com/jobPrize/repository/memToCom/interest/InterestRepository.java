package com.jobPrize.repository.memToCom.interest;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jobPrize.entity.memToCom.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long> {
	 boolean existsByCompanyIdAndMemberId(Long companyId, Long memberId);
	 Page<Interest> findByCompanyId(Long companyId, Pageable pageable);
	 Optional<Interest> findByCompanyIdAndMemberId(Long companyId, Long memberId);
}
