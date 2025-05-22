package com.jobPrize.companyService.service.jobPostingImage.api;

import java.util.List;

public interface JobPostingImageActionService {
    void updateSpecificImages(Long jobPostingId, List<String> imageUrlsToDelete, List<String> newImageUrls);
    void deleteSingleImage(Long imageId);
}