package com.jobPrize.companyService.service;

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
    public void toggleInterestForMember(String token, Long memberId) {
        Long companyId = getCompanyIdFromToken(token);
        Member member = findValidMemberById(memberId);
        Company company = findValidCompanyById(companyId);

        Optional<Interest> existingInterest = interestRepository.findByCompanyIdAndMemberId(companyId, memberId);

        if (existingInterest.isPresent()) {
            // ✅ 관심 등록이 이미 되어 있으면 삭제
            interestRepository.delete(existingInterest.get());
        } else {
            // ✅ 관심 등록이 없으면 새로 추가
            interestRepository.save(Interest.builder().company(company).member(member).build());
        }
    }
    @Transactional
    public void toggleInterestForApplication(String token, Long applicationId) {
        Long companyId = getCompanyIdFromToken(token);
        Application application = findValidApplicationById(applicationId);
        Company company = findValidCompanyById(companyId);

        Optional<Interest> existingInterest = interestRepository.findByCompanyIdAndApplicationId(companyId, applicationId);

        if (existingInterest.isPresent()) {
            // ✅ 관심 등록이 이미 되어 있으면 삭제
            interestRepository.delete(existingInterest.get());
        } else {
            // ✅ 관심 등록이 없으면 새로 추가
            interestRepository.save(Interest.builder().company(company).application(application).build());
        }
    }
   
    @Transactional(readOnly = true)
    public Page<Interest> getInterestedMembers(Long companyId, Pageable pageable) {
        return interestRepository.findAllByCompanyId(companyId, pageable, true); // ✅ 회원(Member) 기준 조회
    }

    @Transactional(readOnly = true)
    public Page<Interest> getInterestedApplications(Long companyId, Pageable pageable) {
        return interestRepository.findAllByCompanyId(companyId, pageable, false); // ✅ 지원서(Application) 기준 조회
    }

    // 🔹 기업 ID 가져오기
    private Long getCompanyIdFromToken(String token) {
        return Long.parseLong(token); 
    }

    // 🔹 회원 조회 메서드
    private Member findValidMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("회원 정보를 찾을 수 없습니다."));
    }

    // 🔹 지원서 조회 메서드
    private Application findValidApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("지원서 정보를 찾을 수 없습니다."));
    }

    // 🔹 기업 조회 메서드
    private Company findValidCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalStateException("회사 정보를 찾을 수 없습니다."));
    }
}