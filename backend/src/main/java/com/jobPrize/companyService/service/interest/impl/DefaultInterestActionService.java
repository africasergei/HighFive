package com.jobPrize.companyService.service.interest.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobPrize.entity.company.Company;
import com.jobPrize.entity.memToCom.Application;
import com.jobPrize.entity.memToCom.Interest;
import com.jobPrize.entity.member.Member;
import com.jobPrize.repository.company.company.CompanyRepository;
import com.jobPrize.repository.memToCom.application.ApplicationRepository;
import com.jobPrize.repository.memToCom.interest.InterestRepository;
import com.jobPrize.repository.member.member.MemberRepository;
import com.jobPrize.companyService.service.interest.api.InterestActionService;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultInterestActionService implements InterestActionService {

    private final InterestRepository interestRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    @Transactional
    public void toggleInterestForMember(Long companyId, Long memberId) {
        Member member = findValidMemberById(memberId);
        Company company = findValidCompanyById(companyId);

        Optional<Interest> existingInterest = interestRepository.findByCompanyIdAndMemberId(companyId, memberId);

        if (existingInterest.isPresent()) {
            interestRepository.delete(existingInterest.get());
        } else {
            interestRepository.save(Interest.builder().company(company).member(member).build());
        }
    }

    @Override
    @Transactional
    public void toggleInterestForApplication(Long companyId, Long applicationId) {
        Application application = findValidApplicationById(applicationId);
        Company company = findValidCompanyById(companyId);

        Optional<Interest> existingInterest = interestRepository.findByCompanyIdAndApplicationId(companyId, applicationId);

        if (existingInterest.isPresent()) {
            interestRepository.delete(existingInterest.get());
        } else {
            interestRepository.save(Interest.builder().company(company).application(application).build());
        }
    }

    private Member findValidMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("회원 정보를 찾을 수 없습니다."));
    }

    private Application findValidApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("지원서 정보를 찾을 수 없습니다."));
    }

    private Company findValidCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalStateException("회사 정보를 찾을 수 없습니다."));
    }
}