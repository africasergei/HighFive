package com.jobPrize.companyService.service.companyInfo.api;

import com.jobPrize.entity.company.Company;
import com.jobPrize.companyService.dto.companyInfoDto.CompanyInfoUpdateDto;

public interface CompanyUpdateService {
    Company updateMyCompanyInfo(Long userId, CompanyInfoUpdateDto dto);
}