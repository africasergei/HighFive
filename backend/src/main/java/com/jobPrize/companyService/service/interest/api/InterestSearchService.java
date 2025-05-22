package com.jobPrize.companyService.service.interest.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.jobPrize.entity.memToCom.Interest;

public interface InterestSearchService {
    boolean isMemberInterested(Long companyId, Long memberId);
    boolean isApplicationInterested(Long companyId, Long applicationId);
    Page<Interest> getInterestedMembers(Long companyId, Pageable pageable);
    Page<Interest> getInterestedApplications(Long companyId, Pageable pageable);
}