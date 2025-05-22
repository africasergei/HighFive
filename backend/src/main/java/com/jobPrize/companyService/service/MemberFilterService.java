package com.jobPrize.companyService.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jobPrize.companyService.dto.memberPool.MemberFilterCondition;
import com.jobPrize.entity.member.Member;
import com.jobPrize.repository.company.memberFilter.MemberFilterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberFilterService {

    private final MemberFilterRepository memberFilterRepository;

    // ✅ 필터링된 데이터 조회
    public Page<Member> findAllByCondition(MemberFilterCondition condition, Pageable pageable) {
        validateFilterCondition(condition);  
        return memberFilterRepository.findAllByCondition(condition, pageable);
    }

    // ✅ 필터 조건이 없을 때 기본 리스트 반환
    public Page<Member> getDefaultMemberList(Pageable pageable) {
        return memberFilterRepository.findAll(pageable);
    }

    // ✅ 최소 하나의 필터 조건이 입력되었는지 확인
    private void validateFilterCondition(MemberFilterCondition condition) {
        boolean isValid = condition.isHasCareer() || condition.getEducation() != null ||
                          (condition.getAddress() != null && !condition.getAddress().isBlank()) ||
                          (condition.getJob() != null && !condition.getJob().isBlank());

        if (!isValid) {
            throw new IllegalArgumentException("최소 하나 이상의 필터 조건을 선택해야 합니다.");
        }
    }
}
