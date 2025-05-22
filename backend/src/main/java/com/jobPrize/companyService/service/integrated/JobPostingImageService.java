package com.jobPrize.companyService.service.integrated;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobPrize.entity.company.JobPosting;
import com.jobPrize.entity.company.JobPostingImage;
import com.jobPrize.repository.company.jobPostingImage.CompanyJobPostingImageRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobPostingImageService {

    private final CompanyJobPostingImageRepository companyJobPostingImageRepository;

    // ✅ 이미지 URL만 반환
    public List<String> getImageUrlsByJobPostingId(Long jobPostingId) {
        return companyJobPostingImageRepository.findAllByJobPostingId(jobPostingId)
                .stream()
                .map(JobPostingImage::getImageUrl)
                .collect(Collectors.toList());
    }

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

    public void deleteSingleImage(Long imageId) {
        companyJobPostingImageRepository.deleteById(imageId);
    }
    
    public List<String> getImageUrlsToDelete(Long jobPostingId, List<String> newImageUrls) {

    	List<String> existingImageUrls = companyJobPostingImageRepository.findAllByJobPostingId(jobPostingId)
                .stream().map(JobPostingImage::getImageUrl)
                .collect(Collectors.toList());

        // ✅ 기존 이미지 목록에서 새로운 목록에 없는 항목을 필터링하여 삭제 리스트 생성
        return existingImageUrls.stream()
                .filter(url -> !newImageUrls.contains(url)) // ❌ 기존에 있었지만 새 리스트에 없는 이미지 찾기
                .collect(Collectors.toList());
    }

}
