package com.jobPrize.companyService.service.jobPostingImage.api;

import java.util.List;

public interface JobPostingImageSearchService {
    List<String> getImageUrlsByJobPostingId(Long jobPostingId);
    List<String> getImageUrlsToDelete(Long jobPostingId, List<String> newImageUrls);
}