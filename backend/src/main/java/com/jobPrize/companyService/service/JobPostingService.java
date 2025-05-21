package com.jobPrize.companyService.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.jobPosting.JobPostingCreateDto;
import com.jobPrize.companyService.dto.jobPosting.JobPostingDetailResponseDto;
import com.jobPrize.companyService.dto.jobPosting.JobPostingUpdateDto;
import com.jobPrize.entity.common.User;
import com.jobPrize.entity.company.Company;
import com.jobPrize.entity.company.JobPosting;
import com.jobPrize.jwt.TokenProvider;
import com.jobPrize.repository.common.user.UserRepository;
import com.jobPrize.repository.company.jobPosting.CompanyJobPostingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final CompanyJobPostingRepository companyJobPostingRepository;
    private final JobPostingImageService jobPostingImageService;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Transactional // ✅ 채용공고 생성
    public JobPosting createJobPosting(String token, JobPostingCreateDto dto) {
        Long userId = getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));

        Company company = user.getCompany();
        if (company == null) {
            throw new IllegalStateException("기업 정보가 없습니다.");
        }

        JobPosting jobPosting = JobPosting.builder()
                .company(company)
                .title(dto.getTitle())
                .workingHours(dto.getWorkingHours())
                .workLocation(dto.getWorkLocation())
                .job(dto.getJob())
                .careerType(dto.getCareerType())
                .educationLevel(dto.getEducationLevel())
                .salary(dto.getSalary())
                .content(dto.getContent())
                .requirement(dto.getRequirement())
                .build();

        JobPosting saved = companyJobPostingRepository.save(jobPosting);

        jobPostingImageService.updateSpecificImages(saved.getId(), List.of(), dto.getImageUrls());

        return saved;
    }

    @Transactional(readOnly = true) // ✅ 채용공고 상세 조회
    public JobPostingDetailResponseDto readJobPostingDetail(Long jobPostingId) {
        JobPosting jobPosting = companyJobPostingRepository.findWithJobPostingImageByJobPostingId(jobPostingId)
                .orElseThrow(() -> new IllegalStateException("해당 채용공고를 찾을 수 없습니다."));

        List<String> imageUrls = jobPostingImageService.getImageUrlsByJobPostingId(jobPostingId);

        return JobPostingDetailResponseDto.builder()
                .companyId(jobPosting.getCompany().getId())
                .jobPostingId(jobPosting.getId())
                .title(jobPosting.getTitle())
                .companyType(jobPosting.getCompanyType())
                .workingHours(jobPosting.getWorkingHours())
                .workLocation(jobPosting.getWorkLocation())
                .job(jobPosting.getJob())
                .careerType(jobPosting.getCareerType())
                .educationLevel(jobPosting.getEducationLevel())
                .salary(jobPosting.getSalary())
                .content(jobPosting.getContent())
                .requirement(jobPosting.getRequirement())
                .imageUrls(imageUrls)
                .build();
    }

    @Transactional // ✅ 채용공고 삭제
    public void deleteJobPosting(String token, Long jobPostingId) {
        Long companyId = getCompanyIdFromToken(token);

        JobPosting jobPosting = companyJobPostingRepository.findWithJobPostingImageByJobPostingId(jobPostingId)
                .orElseThrow(() -> new IllegalStateException("해당 채용공고를 찾을 수 없거나, 삭제 권한이 없습니다."));

        if (!jobPosting.getCompany().getId().equals(companyId)) {
            throw new IllegalStateException("이 공고를 삭제할 권한이 없습니다.");
        }

        List<String> imageUrls = jobPostingImageService.getImageUrlsByJobPostingId(jobPostingId);
        jobPostingImageService.updateSpecificImages(jobPostingId, imageUrls, List.of());

        companyJobPostingRepository.delete(jobPosting);
    }

    @Transactional // ✅ 채용공고 수정
    public JobPosting updateJobPosting(String token, JobPostingUpdateDto dto) {
        Long companyId = getCompanyIdFromToken(token);

        JobPosting jobPosting = companyJobPostingRepository.findWithJobPostingImageByJobPostingId(dto.getJobpostingId())
                .orElseThrow(() -> new IllegalStateException("해당 채용공고를 찾을 수 없거나, 수정 권한이 없습니다."));

        if (!jobPosting.getCompany().getId().equals(companyId)) {
            throw new IllegalStateException("이 공고를 수정할 권한이 없습니다.");
        }

        jobPosting.updateJobPostingInfo(
                dto.getTitle(),
                dto.getContent(),
                dto.getJob(),
                dto.getWorkingHours(),
                dto.getWorkLocation(),
                dto.getCareerType(),
                dto.getEducationLevel(),
                dto.getSalary(),
                dto.getRequirement()
        );

        List<String> toDelete = jobPostingImageService.getImageUrlsToDelete(dto.getJobpostingId(), dto.getImageUrls());
        jobPostingImageService.updateSpecificImages(dto.getJobpostingId(), toDelete, dto.getImageUrls());

        return companyJobPostingRepository.save(jobPosting);
    }

    // 🔹 안정적인 사용자 ID 가져오기
    private Long getUserIdFromToken(String token) {
        try {
            return tokenProvider.getIdFromToken(token);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("토큰에서 유효한 사용자 ID를 찾을 수 없습니다.", e);
        }
    }

    // 🔹 안정적인 기업 ID 가져오기
    private Long getCompanyIdFromToken(String token) {
        Long userId = getUserIdFromToken(token);
        return userRepository.findById(userId)
                .map(user -> user.getCompany().getId())
                .orElseThrow(() -> new IllegalStateException("사용자의 기업 정보를 찾을 수 없습니다."));
    }
}