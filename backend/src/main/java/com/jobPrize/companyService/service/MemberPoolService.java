package com.jobPrize.companyService.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.MemberFilterCondition;
import com.jobPrize.companyService.dto.MemberPoolDto;
import com.jobPrize.entity.common.User;
import com.jobPrize.entity.member.Member;
import com.jobPrize.entity.member.Career;
import com.jobPrize.entity.member.Education;
import com.jobPrize.entity.memToCom.Similarity;
import com.jobPrize.entity.company.JobPosting;
import com.jobPrize.repository.member.member.MemberRepository;
import com.jobPrize.repository.member.career.CareerRepository;
import com.jobPrize.repository.member.education.EducationRepository;
import com.jobPrize.repository.memToCom.similarity.SimilarityRepository;

@Service
@Transactional(readOnly = true)
public class MemberPoolService {
    private final MemberRepository memberRepository;
    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;
    private final SimilarityRepository similarityRepository;

    public MemberPoolService(MemberRepository memberRepository,
                            CareerRepository careerRepository,
                            EducationRepository educationRepository,
                            SimilarityRepository similarityRepository) {
        this.memberRepository = memberRepository;
        this.careerRepository = careerRepository;
        this.educationRepository = educationRepository;
        this.similarityRepository = similarityRepository;
    }

    /**
     * 멤버 풀 리스트 조회
     * @param filterCondition 필터 조건
     * @return 멤버 풀 DTO 리스트
     */
    public List<MemberPoolDto> getMemberPoolList(MemberFilterCondition filterCondition) {
        // 1. 기본 쿼리 생성
        List<Member> members = memberRepository.findAll();

        // 2. 검색 조건 적용
        if (filterCondition.getSearchKeyword() != null && !filterCondition.getSearchKeyword().isEmpty()) {
            String keyword = filterCondition.getSearchKeyword().toLowerCase();
            switch (filterCondition.getSearchType()) {
                case "name":
                    members = members.stream()
                        .filter(m -> m.getUser().getName().toLowerCase().contains(keyword))
                        .collect(Collectors.toList());
                    break;
                case "job":
                    members = members.stream()
                        .filter(m -> m.getCareers().stream()
                            .anyMatch(c -> c.getJob().toLowerCase().contains(keyword)))
                        .collect(Collectors.toList());
                    break;
                case "company":
                    members = members.stream()
                        .filter(m -> m.getCareers().stream()
                            .anyMatch(c -> c.getCompanyName().toLowerCase().contains(keyword)))
                        .collect(Collectors.toList());
                    break;
            }
        }

        // 3. 필터 조건 적용
        if (filterCondition.isHasCareer()) {
            members = members.stream()
                .filter(m -> !m.getCareers().isEmpty())
                .collect(Collectors.toList());
        }

        if (filterCondition.getEducation() != null) {
            members = members.stream()
                .filter(m -> m.getEducations().stream()
                    .anyMatch(e -> e.getEducationLevel().equals(filterCondition.getEducation())))
                .collect(Collectors.toList());
        }

        // 4. 관심 필터 적용
        if (filterCondition.isFavoriteOnly()) {
            // TODO: 관심 필터링 로직 구현
            // 현재는 예시로 모든 멤버를 반환
        }

        // 5. 정렬 조건 적용
        if (filterCondition.getSortBy() != null) {
            switch (filterCondition.getSortBy()) {
                case "similarity":
                    members = members.stream()
                        .sorted((m1, m2) -> {
                            int score1 = getSimilarityScore(m1.getId());
                            int score2 = getSimilarityScore(m2.getId());
                            return filterCondition.isDescending() ? Integer.compare(score2, score1) : Integer.compare(score1, score2);
                        })
                        .collect(Collectors.toList());
                    break;
                case "favoriteCount":
                    members = members.stream()
                        .sorted((m1, m2) -> {
                            int count1 = getFavoriteCount(m1.getId());
                            int count2 = getFavoriteCount(m2.getId());
                            return filterCondition.isDescending() ? Integer.compare(count2, count1) : Integer.compare(count1, count2);
                        })
                        .collect(Collectors.toList());
                    break;
            }
        }

        // 6. DTO 변환
        return members.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    /**
     * 멤버를 DTO로 변환
     * @param member 멤버 엔티티
     * @return 멤버 풀 DTO
     */
    private MemberPoolDto convertToDto(Member member) {
        List<Career> careers = careerRepository.findByMemberId(member.getId());
        List<Education> educations = educationRepository.findByMemberId(member.getId());
        Page<Similarity> similarities = similarityRepository.findAllWithJobPostingByMemberId(member.getId(), PageRequest.of(0, 10));

        return MemberPoolDto.builder()
            .userId(member.getId())
            .name(member.getUser().getName())
            .email(member.getUser().getEmail())
            .phone(member.getUser().getPhone())
            .address(member.getUser().getAddress())
            .nickname(member.getNickname())
            .companyNames(careers.stream().map(Career::getCompanyName).collect(Collectors.toList()))
            .jobs(careers.stream().map(Career::getJob).collect(Collectors.toList()))
            .departments(careers.stream().map(Career::getDepartment).collect(Collectors.toList()))
            .positions(careers.stream().map(Career::getPosition).collect(Collectors.toList()))
            .schoolNames(educations.stream().map(Education::getSchoolName).collect(Collectors.toList()))
            .majors(educations.stream().map(Education::getMajor).collect(Collectors.toList()))
            .educationLevels(educations.stream().map(Education::getEducationLevel).map(Enum::name).collect(Collectors.toList()))
            .similarityScore(similarities.isEmpty() ? null : similarities.getContent().get(0).getScore())
            .jobPostingId(similarities.isEmpty() ? null : similarities.getContent().get(0).getJobPosting().getId())
            .similarJobPostingIds(similarities.getContent().stream()
                .map(Similarity::getJobPosting)
                .map(JobPosting::getId)
                .collect(Collectors.toList()))
            .isFavorite(getIsFavorite(member.getId()))
            .favoriteCount(getFavoriteCount(member.getId()))
            .build();
    }

    /**
     * 유사도 점수 조회
     * @param memberId 멤버 ID
     * @return 유사도 점수
     */
    private int getSimilarityScore(Long memberId) {
        return similarityRepository.findAllWithJobPostingByMemberId(memberId, PageRequest.of(0, 1))
            .getContent()
            .stream()
            .mapToInt(Similarity::getScore)
            .findFirst()
            .orElse(0);
    }

    /**
     * 관심 여부 조회
     * @param memberId 멤버 ID
     * @return 관심 여부
     */
    private boolean getIsFavorite(Long memberId) {
        // TODO: 관심 여부 조회 로직 구현
        return false;
    }

    /**
     * 관심 수 조회
     * @param memberId 멤버 ID
     * @return 관심 수
     */
    private int getFavoriteCount(Long memberId) {
        // TODO: 관심 수 조회 로직 구현
        return 0;
    }
}
