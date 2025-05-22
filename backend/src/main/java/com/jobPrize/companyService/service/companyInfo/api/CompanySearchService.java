package com.jobPrize.companyService.service.companyInfo.api;

import java.util.Optional;
import com.jobPrize.entity.company.Company;

public interface CompanySearchService {
    Optional<Company> findMyCompany(Long userId);
}