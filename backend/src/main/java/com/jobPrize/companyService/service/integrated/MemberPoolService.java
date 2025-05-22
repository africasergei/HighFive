package com.jobPrize.companyService.service.integrated;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.memberPool.MemberDetailResponseDto;
import com.jobPrize.companyService.dto.memberPool.MemberPoolListResponseDto;
import com.jobPrize.entity.common.User;
import com.jobPrize.entity.company.JobPosting;
import com.jobPrize.entity.memToCom.Interest;
import com.jobPrize.entity.memToCom.Similarity;
import com.jobPrize.entity.member.Education;
import com.jobPrize.entity.member.Member;
import com.jobPrize.repository.common.subscription.SubscriptionRepository;
import com.jobPrize.repository.common.user.UserRepository;
import com.jobPrize.repository.company.jobPosting.CompanyJobPostingRepository;
import com.jobPrize.repository.memToCom.similarity.SimilarityRepository;
import com.jobPrize.repository.member.education.EducationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberPoolService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SimilarityRepository similarityRepository;
    private final EducationRepository educationRepository;
    private final CompanyJobPostingRepository companyJobPostingRepository;
    private final InterestService interestService;

    // ✅ 기업이 인재 검색 (리스트 조회)
    @Transactional(readOnly = true)
    public Page<MemberPoolListResponseDto> getMemberInfo(Long companyId, Pageable pageable) {
        JobPosting latestJobPosting = companyJobPostingRepository.findLatestJobPosting(companyId)
                .orElseThrow(() -> new IllegalStateException("최근 채용공고를 찾을 수 없습니다."));

        Page<Similarity> similaritiesPage = similarityRepository.findAllWithMemberByJobPostingId(latestJobPosting.getId(), pageable);

        return similaritiesPage.map(similarity -> {
            Member member = similarity.getMember();
            List<Education> educations = educationRepository.findAllByMemberId(member.getId());
            boolean isInterested = interestService.isMemberInterested(companyId, member.getId()); // ✅ 관심 여부 체크
            return mapToDto(member, educations, similarity, isInterested);
        });
    }

    // ✅ 기업이 특정 인재 상세 조회
    @Transactional(readOnly = true)
    public MemberDetailResponseDto getMemberDetail(Long companyId, Long memberId) {
        Member member = findValidMemberById(memberId);
        boolean isInterested = interestService.isMemberInterested(companyId, memberId); // ✅ 관심 여부 체크

        return mapToDetailDto(member, isInterested);
    }

    // ✅ 기업이 특정 인재를 관심 등록 (토글 방식 적용)
    @Transactional
    public void toggleInterestForMember(Long companyId, Long memberId) {
        interestService.toggleInterestForMember(companyId, memberId); // ✅ 기존 `InterestService` 활용
    }

    // ✅ 기업이 관심 등록한 인재 조회
    @Transactional(readOnly = true)
    public Page<MemberPoolListResponseDto> getInterestedMembers(Long companyId, Pageable pageable) {
        Page<Interest> interestedMembers = interestService.getInterestedMembers(companyId, pageable); // ✅ 관심 목록 조회

        return interestedMembers.map(interest -> {
            Member member = interest.getMember();
            List<Education> educations = educationRepository.findAllByMemberId(member.getId());
            return mapToDto(member, educations, null, true);
        });
    }

    // 🔹 DTO 변환 로직
    private MemberPoolListResponseDto mapToDto(Member member, List<Education> educations, Similarity similarity, boolean isInterested) {
        return MemberPoolListResponseDto.builder()
                .memberId(member.getUser().getId())
                .name(member.getUser().getName())
                .job(member.getCareers().isEmpty() ? null : member.getCareers().get(0).getJob())
                .hasCareer(!member.getCareers().isEmpty())
                .educationLevel(educations.isEmpty() ? null : educations.get(0).getEducationLevel())
                .score(similarity != null ? similarity.getScore() : 0)
                .isInterested(isInterested)
                .build();
    }

    private MemberDetailResponseDto mapToDetailDto(Member member, boolean isInterested) {
        return MemberDetailResponseDto.builder()
                .id(member.getUser().getId())
                .name(member.getUser().getName())
                .email(member.getUser().getEmail())
                .gender(member.getUser().getGenderType())
                .birthDate(member.getUser().getBirthDate())
                .phone(member.getUser().getPhone())
                .isInterested(isInterested)
                .build();
    }

    private Member findValidMemberById(Long memberId) {
        return userRepository.findById(memberId)
                .map(User::getMember)
                .orElseThrow(() -> new IllegalStateException("해당 회원을 찾을 수 없습니다."));
    }
}