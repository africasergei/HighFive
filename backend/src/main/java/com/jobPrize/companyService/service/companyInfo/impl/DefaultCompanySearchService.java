package com.jobPrize.companyService.service.companyInfo.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jobPrize.companyService.service.companyInfo.api.CompanySearchService;
import com.jobPrize.entity.company.Company;
import com.jobPrize.repository.company.company.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultCompanySearchService implements CompanySearchService {
    
    private final CompanyRepository companyRepository;

    @Override
    public Optional<Company> findMyCompany(Long userId) {
        return companyRepository.findByUserId(userId);
    }
}