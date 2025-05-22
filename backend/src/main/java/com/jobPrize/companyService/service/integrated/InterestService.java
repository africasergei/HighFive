package com.jobPrize.companyService.service.integrated;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    public boolean isMemberInterested(Long companyId, Long memberId) {
        return interestRepository.existsByCompanyIdAndMemberId(companyId, memberId);
    }

    @Transactional(readOnly = true)
    public boolean isApplicationInterested(Long companyId, Long applicationId) {
        return interestRepository.existsByCompanyIdAndApplicationId(companyId, applicationId);
    }

    @Transactional
    public void toggleInterestForMember(Long companyId, Long memberId) {
        Member member = findValidMemberById(memberId);
        Company company = findValidCompanyById(companyId);

        Optional<Interest> existingInterest = interestRepository.findByCompanyIdAndMemberId(companyId, memberId);

        if (existingInterest.isPresent()) {
            interestRepository.delete(existingInterest.get()); // ✅ 기존 관심 등록 삭제
        } else {
            interestRepository.save(Interest.builder().company(company).member(member).build()); // ✅ 새로운 관심 등록
        }
    }

    @Transactional
    public void toggleInterestForApplication(Long companyId, Long applicationId) {
        Application application = findValidApplicationById(applicationId);
        Company company = findValidCompanyById(companyId);

        Optional<Interest> existingInterest = interestRepository.findByCompanyIdAndApplicationId(companyId, applicationId);

        if (existingInterest.isPresent()) {
            interestRepository.delete(existingInterest.get()); // ✅ 기존 관심 등록 삭제
        } else {
            interestRepository.save(Interest.builder().company(company).application(application).build()); // ✅ 새로운 관심 등록
        }
    }

    @Transactional(readOnly = true)
    public Page<Interest> getInterestedMembers(Long companyId, Pageable pageable) {
        return interestRepository.findAllByCompanyId(companyId, pageable, true); // ✅ 회원 기준 조회
    }

    @Transactional(readOnly = true)
    public Page<Interest> getInterestedApplications(Long companyId, Pageable pageable) {
        return interestRepository.findAllByCompanyId(companyId, pageable, false); // ✅ 지원서 기준 조회
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