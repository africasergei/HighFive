package com.jobPrize.companyService.service.companyInfo.impl;

import org.springframework.stereotype.Service;

import com.jobPrize.companyService.dto.companyInfoDto.CompanyInfoUpdateDto;
import com.jobPrize.companyService.service.companyInfo.api.CompanySearchService;
import com.jobPrize.companyService.service.companyInfo.api.CompanyUpdateService;
import com.jobPrize.entity.company.Company;
import com.jobPrize.repository.company.company.CompanyRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultCompanyUpdateService implements CompanyUpdateService {
    
    private final CompanyRepository companyRepository;
    private final CompanySearchService companySearchService; 

    @Override
    public Company updateMyCompanyInfo(Long userId, CompanyInfoUpdateDto dto) {
        Company company = companySearchService.findMyCompany(userId)
                .orElseThrow(() -> new EntityNotFoundException("기업 정보를 찾을 수 없습니다."));

        return companyRepository.save(
                Company.builder()
                        .id(company.getId())
                        .companyName(dto.getCompanyName())
                        .representativeName(dto.getRepresentativeName())
                        .businessNumber(dto.getBusinessNumber())
                        .establishedDate(dto.getEstablishedDate())
                        .employeeCount(dto.getEmployeeCount())
                        .industry(dto.getIndustry())
                        .companyAddress(dto.getCompanyAddress())
                        .companyPhone(dto.getCompanyPhone())
                        .companyEmail(dto.getCompanyEmail())
                        .introduction(dto.getIntroduction())
                        .build()
        );
    }
}