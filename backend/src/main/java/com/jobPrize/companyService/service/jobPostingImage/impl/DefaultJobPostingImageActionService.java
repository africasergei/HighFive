package com.jobPrize.companyService.service.jobPostingImage.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobPrize.repository.company.jobPostingImage.CompanyJobPostingImageRepository;
import com.jobPrize.companyService.service.jobPostingImage.api.JobPostingImageActionService;
import com.jobPrize.entity.company.JobPosting;
import com.jobPrize.entity.company.JobPostingImage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultJobPostingImageActionService implements JobPostingImageActionService {

    private final CompanyJobPostingImageRepository companyJobPostingImageRepository;

    @Override
    @Transactional
    public void updateSpecificImages(Long jobPostingId, List<String> imageUrlsToDelete, List<String> newImageUrls) {
        List<JobPostingImage> images = companyJobPostingImageRepository.findAllByJobPostingId(jobPostingId);

        for (JobPostingImage image : images) {
            if (imageUrlsToDelete.contains(image.getImageUrl())) {
                companyJobPostingImageRepository.delete(image);
            }
        }

        List<JobPostingImage> newImages = newImageUrls.stream()
                .map(imageUrl -> JobPostingImage.builder()
                        .jobPosting(JobPosting.builder().id(jobPostingId).build())
                        .imageUrl(imageUrl)
                        .build())
                .collect(Collectors.toList());

        companyJobPostingImageRepository.saveAll(newImages);
    }

    @Override
    public void deleteSingleImage(Long imageId) {
        companyJobPostingImageRepository.deleteById(imageId);
    }
}