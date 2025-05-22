package com.jobPrize.companyService.service.interest.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobPrize.entity.memToCom.Interest;
import com.jobPrize.repository.memToCom.interest.InterestRepository;
import com.jobPrize.companyService.service.interest.api.InterestSearchService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultInterestSearchService implements InterestSearchService {

    private final InterestRepository interestRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean isMemberInterested(Long companyId, Long memberId) {
        return interestRepository.existsByCompanyIdAndMemberId(companyId, memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isApplicationInterested(Long companyId, Long applicationId) {
        return interestRepository.existsByCompanyIdAndApplicationId(companyId, applicationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Interest> getInterestedMembers(Long companyId, Pageable pageable) {
        return interestRepository.findAllByCompanyId(companyId, pageable, true);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Interest> getInterestedApplications(Long companyId, Pageable pageable) {
        return interestRepository.findAllByCompanyId(companyId, pageable, false);
    }
}