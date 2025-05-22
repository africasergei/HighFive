package com.jobPrize.companyService.service.integrated;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jobPrize.companyService.dto.companyInfoDto.CompanyInfoUpdateDto;
import com.jobPrize.entity.company.Company;
import com.jobPrize.repository.company.company.CompanyRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    // ✅ 기업 정보 조회
    public Optional<Company> findMyCompany(Long userId) {
        return companyRepository.findByUserId(userId);
    }

    // ✅ 기업 정보 수정
    public Company updateMyCompanyInfo(Long userId, CompanyInfoUpdateDto dto) {
        Company company = findMyCompany(userId)
                .orElseThrow(() -> new EntityNotFoundException("기업 정보를 찾을 수 없습니다.")); 

        // ✅ 기존 데이터를 유지하면서 빌더를 활용하여 업데이트
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