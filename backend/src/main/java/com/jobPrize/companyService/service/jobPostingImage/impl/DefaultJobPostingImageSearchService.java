package com.jobPrize.companyService.service.jobPostingImage.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.jobPrize.repository.company.jobPostingImage.CompanyJobPostingImageRepository;
import com.jobPrize.companyService.service.jobPostingImage.api.JobPostingImageSearchService;
import com.jobPrize.entity.company.JobPostingImage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultJobPostingImageSearchService implements JobPostingImageSearchService {

    private final CompanyJobPostingImageRepository companyJobPostingImageRepository;

    @Override
    public List<String> getImageUrlsByJobPostingId(Long jobPostingId) {
        return companyJobPostingImageRepository.findAllByJobPostingId(jobPostingId)
                .stream()
                .map(JobPostingImage::getImageUrl)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getImageUrlsToDelete(Long jobPostingId, List<String> newImageUrls) {
        List<String> existingImageUrls = companyJobPostingImageRepository.findAllByJobPostingId(jobPostingId)
                .stream()
                .map(JobPostingImage::getImageUrl)
                .collect(Collectors.toList());

        return existingImageUrls.stream()
                .filter(url -> !newImageUrls.contains(url))
                .collect(Collectors.toList());
    }
}