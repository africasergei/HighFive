package com.jobPrize.service.company.jobPosting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jobPrize.customException.CustomEntityNotFoundException;
import com.jobPrize.dto.company.jobPosting.JobPostingCreateDto;
import com.jobPrize.dto.company.jobPosting.JobPostingResponseDto;
import com.jobPrize.dto.company.jobPosting.JobPostingSummaryDto;
import com.jobPrize.dto.company.jobPosting.JobPostingUpdateDto;
import com.jobPrize.dto.company.jobPostingImage.JobPostingImageCreateDto;
import com.jobPrize.dto.company.jobPostingImage.JobPostingImageCreateListDto;
import com.jobPrize.entity.company.Company;
import com.jobPrize.entity.company.JobPosting;
import com.jobPrize.entity.company.JobPostingImage;
import com.jobPrize.enumerate.ApprovalStatus;
import com.jobPrize.enumerate.UserType;
import com.jobPrize.repository.company.company.CompanyRepository;
import com.jobPrize.repository.company.jobPosting.JobPostingRepository;
import com.jobPrize.service.company.jobPostingImage.JobPostingImageService;
import com.jobPrize.util.AssertUtil;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class JobPostingServiceImpl implements JobPostingService{
	
	private final JobPostingRepository jobPostingRepository;

	private final CompanyRepository companyRepository;
	
	private final JobPostingImageService jobPostingImageService;

	private final AssertUtil assertUtil;

	private final static String ENTITY_NAME = "채용 공고";

	@Override
	public void createJobPosting(Long id, UserType userType, ApprovalStatus approvalStatus, boolean isSubscribed, JobPostingCreateDto jobPostingCreateDto, JobPostingImageCreateListDto jobPostingImageCreateListDto) {
		
		String action = "등록";

		assertUtil.assertForCompany(userType, approvalStatus, isSubscribed, ENTITY_NAME, action);
		
		Company company = companyRepository.findByIdAndDeletedDateIsNull(id)
				.orElseThrow(() -> new CustomEntityNotFoundException("기업"));
		
		JobPosting jobPosting = JobPosting.of(company,jobPostingCreateDto);
		
		jobPostingRepository.save(jobPosting);
		
		List<JobPostingImageCreateDto> jobPostingImageCreateDtos = jobPostingImageCreateListDto.getJobPostingImageCreateDtos();
		List<MultipartFile> multipartFiles = new ArrayList<>();
		for(JobPostingImageCreateDto jobPostingImageCreateDto : jobPostingImageCreateDtos) {
			MultipartFile multipartFile= jobPostingImageCreateDto.getImage();
			multipartFiles.add(multipartFile);
		}
		jobPostingImageService.createImages(jobPosting, multipartFiles);
	}

	@Override
	public Page<JobPostingSummaryDto> readJobPostingPage(Long id, Pageable pageable) {

		Page<JobPosting> jobPostings = jobPostingRepository.findAllByCompanyId(id, pageable);

		List<JobPostingSummaryDto> jobPostingSummaries = new ArrayList<>();
		for(JobPosting jobPosting : jobPostings) {
			JobPostingSummaryDto jobPostingSummaryDto = JobPostingSummaryDto.from(jobPosting);
			jobPostingSummaries.add(jobPostingSummaryDto);
		}
		return new PageImpl<>(jobPostingSummaries, pageable, jobPostings.getTotalElements());
	}

	@Override
	public JobPostingResponseDto readJobPosting(Long jobPostingId) {

		JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
			.orElseThrow(() -> new CustomEntityNotFoundException(ENTITY_NAME));
		
		List<JobPostingImage> jobPostingImages = jobPosting.getJobPostingImages();
		List<String> jobPostingImageUrls = new ArrayList<>();
		
		for(JobPostingImage jobPostingImage:jobPostingImages) {
			String jobPostingImageUrl = "/images/" + jobPostingImage.getImageName();
			jobPostingImageUrls.add(jobPostingImageUrl);
		}
		return JobPostingResponseDto.of(jobPosting, jobPostingImageUrls);
	}

	@Override
	public void updateJobPosting(Long id, UserType userType, ApprovalStatus approvalStatus, boolean isSubscribed,JobPostingUpdateDto jobPostingUpdateDto, JobPostingImageCreateListDto jobPostingImageCreateListDto) {
		
		String action = "수정";

		assertUtil.assertForCompany(userType, approvalStatus, isSubscribed, ENTITY_NAME, action);

		Long jobPostingId = jobPostingUpdateDto.getId();
		
		JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
			.orElseThrow(() -> new CustomEntityNotFoundException(ENTITY_NAME));

		Long ownerId = jobPostingRepository.findCompanyIdByJobPostingId(jobPostingId)
			.orElseThrow(() -> new CustomEntityNotFoundException("소유자"));

		assertUtil.assertId(id, ownerId, ENTITY_NAME, action);
		
		jobPosting.updateJobPostingInfo(jobPostingUpdateDto);
		
		List<JobPostingImageCreateDto> jobPostingImageCreateDtos = jobPostingImageCreateListDto.getJobPostingImageCreateDtos();
		List<MultipartFile> multipartFiles = new ArrayList<>();
		for(JobPostingImageCreateDto jobPostingImageCreateDto : jobPostingImageCreateDtos) {
			MultipartFile multipartFile= jobPostingImageCreateDto.getImage();
			multipartFiles.add(multipartFile);
		}
		jobPostingImageService.createImages(jobPosting, multipartFiles);
	}

	@Override
	public void deleteJobPosting(Long id, Long jobPostingId) {

		String action = "삭제";

		JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
			.orElseThrow(() -> new CustomEntityNotFoundException(ENTITY_NAME));

		Long ownerId = jobPostingRepository.findCompanyIdByJobPostingId(jobPostingId)
			.orElseThrow(() -> new CustomEntityNotFoundException("소유자"));
		
		assertUtil.assertId(id, ownerId, ENTITY_NAME, action);
		
		jobPostingRepository.delete(jobPosting);
	}

}