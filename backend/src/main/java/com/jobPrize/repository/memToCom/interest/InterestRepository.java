package com.jobPrize.repository.memToCom.interest;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobPrize.entity.memToCom.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long> ,InterestRepositoryCustom{
    // ✅ 기업이 특정 회원을 관심 등록했는지 확인
    boolean existsByCompanyIdAndMemberId(Long companyId, Long memberId);

    // ✅ 기업이 특정 회원의 관심 등록 정보를 조회
    Optional<Interest> findByCompanyIdAndMemberId(Long companyId, Long memberId);

    // ✅ 기업이 특정 지원서를 관심 등록했는지 확인
    boolean existsByCompanyIdAndApplicationId(Long companyId, Long applicationId);

    // ✅ 기업이 특정 지원서의 관심 등록 정보를 조회
    Optional<Interest> findByCompanyIdAndApplicationId(Long companyId, Long applicationId);
}