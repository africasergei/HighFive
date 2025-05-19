package com.jobPrize.companyService.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jobPrize.companyService.dto.MemberPoolDto;
import com.jobPrize.entity.common.User;
import com.jobPrize.entity.common.UserType;
import com.jobPrize.entity.company.JobPosting;
import com.jobPrize.entity.memToCom.Similarity;
import com.jobPrize.entity.member.Education;
import com.jobPrize.entity.member.Member;
import com.jobPrize.jwt.TokenProvider;
import com.jobPrize.repository.common.UserRepository;
import com.jobPrize.repository.common.subscription.SubscriptionRepository;
import com.jobPrize.repository.company.jobPosting.CompanyJobPostingRepository;
import com.jobPrize.repository.memToCom.similarity.SimilarityRepository;
import com.jobPrize.repository.member.education.EducationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberPoolService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TokenProvider tokenProvider;
    private final SimilarityRepository similarityRepository;
    private final EducationRepository educationRepository;
    private final CompanyJobPostingRepository companyJobPostingRepository;

    public boolean hasAccessToTalentPool(String token) {
        Long userId = getUserIdFromToken(token);
        UserType userType = tokenProvider.getUserTypeFromToken(token);

        if (!isCompanyUser(userType)) {
            return false;
        }

        User user = findValidUserById(userId);
        return user != null && subscriptionRepository.existsByUserId(user.getId());
    }

    public Page<MemberPoolDto> getMemberInfo(String token, Pageable pageable) throws IllegalAccessException {
        Long userId = getUserIdFromToken(token);
        UserType userType = tokenProvider.getUserTypeFromToken(token);

        if (!isCompanyUser(userType)) {
            throw new IllegalAccessException("접근 권한이 없습니다.");
        }
       
        if (!hasAccessToTalentPool(token)) {
            throw new IllegalAccessException("구독을 하지 않은 사용자는 접근할 수 없습니다.");
        }

        User user = findValidUserById(userId);
        if (user == null) {
            throw new IllegalStateException("사용자를 찾을 수 없습니다.");
        }
        if (user.getCompany() == null) {
            throw new IllegalStateException("사용자가 소속된 기업 정보를 찾을 수 없습니다.");
        }

        Long companyId = user.getCompany().getId();
        JobPosting latestJobPosting = companyJobPostingRepository.findLatestJobPosting(companyId)
                .orElseThrow(() -> new IllegalStateException("최근 채용공고를 찾을 수 없습니다."));

        Page<Similarity> similaritiesPage = similarityRepository.findAllWithMemberByJobPostingId(latestJobPosting.getId(), pageable);

        return similaritiesPage.map(similarity -> {
            Member member = similarity.getMember();
            List<Education> educations = educationRepository.findAllByMemberId(member.getId());
            return mapToDto(member, educations, similarity);
        });
    }

    private Long getUserIdFromToken(String token) {
        return Long.parseLong(tokenProvider.getIdFromToken(token));
    }

    private boolean isCompanyUser(UserType userType) {
        return userType == UserType.기업회원;
    }

    private User findValidUserById(Long userId) {
        return userRepository.findByIdAndDeletedDateIsNull(userId).orElse(null);
    }

    private MemberPoolDto mapToDto(Member member, List<Education> educations, Similarity similarity) {
        return MemberPoolDto.builder()
                .memberId(member.getUser().getId())
                .name(member.getUser().getName())
                .job(member.getCareers().isEmpty() ? null : member.getCareers().get(0).getJob()) 
                .hasCareer(!member.getCareers().isEmpty())
                .educationLevel(educations != null && !educations.isEmpty() ? educations.get(0).getEducationLevel() : null) // ✅ `name()` 제거 및 널 체크 적용
                .score(similarity != null ? similarity.getScore() : 0) 
                .build();
    }
}