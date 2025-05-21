package com.jobPrize.companyService.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jobPrize.companyService.dto.memberPool.MemberDetailResponseDto;
import com.jobPrize.companyService.dto.memberPool.MemberPoolListResponseDto;
import com.jobPrize.entity.common.User;
import com.jobPrize.entity.common.UserType;
import com.jobPrize.entity.company.Company;
import com.jobPrize.entity.company.JobPosting;
import com.jobPrize.entity.memToCom.Interest;
import com.jobPrize.entity.memToCom.Similarity;
import com.jobPrize.entity.member.Career;
import com.jobPrize.entity.member.Certification;
import com.jobPrize.entity.member.Education;
import com.jobPrize.entity.member.LanguageTest;
import com.jobPrize.entity.member.Member;
import com.jobPrize.jwt.TokenProvider;
import com.jobPrize.repository.common.UserRepository;
import com.jobPrize.repository.common.subscription.SubscriptionRepository;
import com.jobPrize.repository.company.jobPosting.CompanyJobPostingRepository;
import com.jobPrize.repository.memToCom.interest.InterestRepository;
import com.jobPrize.repository.memToCom.similarity.SimilarityRepository;
import com.jobPrize.repository.member.career.CareerRepository;
import com.jobPrize.repository.member.certification.CertificationRepository;
import com.jobPrize.repository.member.education.EducationRepository;
import com.jobPrize.repository.member.languageTest.LanguageTestRepository;

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
    private final InterestRepository interestRepository;
    private final CertificationRepository certificationRepository;
    private final LanguageTestRepository languageTestRepository;
    private final CareerRepository careerRepository;
    
    
    
    // ✅ 기업이 인재 검색 (리스트 조회)
    public Page<MemberPoolListResponseDto> getMemberInfo(String token, Pageable pageable) throws IllegalAccessException {
        if (!hasAccessToMemberPool(token)) {
            throw new IllegalAccessException("구독을 하지 않은 사용자는 접근할 수 없습니다.");
        }

        Long userId = getUserIdFromToken(token);
        User user = findValidUserById(userId);
        if (user == null || user.getCompany() == null) {
            throw new IllegalStateException("사용자 정보를 찾을 수 없습니다.");
        }

        JobPosting latestJobPosting = companyJobPostingRepository.findLatestJobPosting(user.getCompany().getId())
                .orElseThrow(() -> new IllegalStateException("최근 채용공고를 찾을 수 없습니다."));

        Page<Similarity> similaritiesPage = similarityRepository.findAllWithMemberByJobPostingId(latestJobPosting.getId(), pageable);

        return similaritiesPage.map(similarity -> {
            Member member = similarity.getMember();
            List<Education> educations = educationRepository.findAllByMemberId(member.getId());
            boolean isInterested = interestRepository.existsByCompanyIdAndMemberId(user.getCompany().getId(), member.getId());
            return mapToDto(member, educations, similarity, isInterested);
        });
    }

    // ✅ 기업이 특정 인재 상세 조회
    public MemberDetailResponseDto getMemberDetail(String token, Long memberId) throws IllegalAccessException {
        if (!hasAccessToMemberPool(token)) {
            throw new IllegalAccessException("구독을 하지 않은 사용자는 접근할 수 없습니다.");
        }

        Member member = findValidMemberById(memberId);
        List<Certification> certifications = certificationRepository.findAllByMemberId(memberId);
        List<LanguageTest> languageTests = languageTestRepository.findAllByMemberId(memberId);

        // ✅ 최신 학력 및 경력 조회
        Education latestEducation = educationRepository.findTopByMemberIdOrderByGraduationDateDesc(memberId)
                .orElse(null);
        Career latestCareer = careerRepository.findTopByMemberIdOrderByStartDateDesc(memberId)
                .orElse(null);

        boolean isInterested = interestRepository.existsByCompanyIdAndMemberId(getCompanyIdFromToken(token), memberId);

        return mapToDetailDto(member, latestEducation, latestCareer, certifications, languageTests, isInterested);
    }


    // ✅ 기업이 특정 인재를 관심 등록
    public void registerInterest(String token, Long memberId) {
        Long companyId = getCompanyIdFromToken(token);
        Member member = findValidMemberById(memberId);

        Interest interest = Interest.builder()
                .company(Company.builder().id(companyId).build())
                .member(member)
                .build();

        interestRepository.save(interest);
    }

    // ✅ 기업이 관심 등록한 인재 조회
    public Page<MemberPoolListResponseDto> getInterestedMembers(String token, Pageable pageable) {
        Long companyId = getCompanyIdFromToken(token);

        Page<Interest> interestedMembers = interestRepository.findByCompanyId(companyId, pageable);

        return interestedMembers.map(interest -> {
            Member member = interest.getMember();
            List<Education> educations = educationRepository.findAllByMemberId(member.getId());
            return mapToDto(member, educations, null, true);
        });
    }

    // ✅ 관심 등록을 취소
    public void removeInterest(String token, Long memberId) {
        Long companyId = getCompanyIdFromToken(token);

        Interest interest = interestRepository.findByCompanyIdAndMemberId(companyId, memberId)
                .orElseThrow(() -> new IllegalStateException("관심 등록되지 않은 인재입니다."));

        interestRepository.delete(interest);
    }

    // 🔹 인증 및 검증 로직
    private boolean hasAccessToMemberPool(String token) {
        Long userId = getUserIdFromToken(token);
        UserType userType = tokenProvider.getUserTypeFromToken(token);

        if (!isCompanyUser(userType)) {
            return false;
        }

        User user = findValidUserById(userId);
        return user != null && subscriptionRepository.existsByUserId(user.getId());
    }

    private Long getUserIdFromToken(String token) {
        return Long.parseLong(tokenProvider.getIdFromToken(token));
    }

    private Long getCompanyIdFromToken(String token) {
        User user = findValidUserById(getUserIdFromToken(token));
        return user.getCompany().getId();
    }

    private boolean isCompanyUser(UserType userType) {
        return userType == UserType.기업회원;
    }

    private User findValidUserById(Long userId) {
        return userRepository.findByIdAndDeletedDateIsNull(userId).orElse(null);
    }

    private Member findValidMemberById(Long memberId) {
        return userRepository.findByIdAndDeletedDateIsNull(memberId)
                .map(User::getMember)
                .orElseThrow(() -> new IllegalStateException("해당 회원을 찾을 수 없습니다."));
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

    private MemberDetailResponseDto mapToDetailDto(Member member, 
            Education latestEducation, 
            Career latestCareer, 
            List<Certification> certifications, 
            List<LanguageTest> languageTests, 
            boolean isInterested) {
        
        return MemberDetailResponseDto.builder()
                .id(member.getUser().getId())
                .name(member.getUser().getName())
                .email(member.getUser().getEmail())
                .gender(member.getUser().getGenderType())
                .age(member.getUser().getAge())
                .phone(member.getUser().getPhone())
                .hasCareer(latestCareer != null) // 최신 경력 존재 여부
                .job(latestCareer != null ? latestCareer.getJob() : null) // 최신 경력의 직무
                .educationResponseDto(latestEducation != null ? EducationResponseDto.fromEntity(latestEducation) : null)
                .careerResponseDto(latestCareer != null ? CareerResponseDto.fromEntity(latestCareer) : null)
                .certificationResponseDto(certifications)
                .languageTestResponseDto(languageTests)
                .isInterested(isInterested)
                .build();
    }

}