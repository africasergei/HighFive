package com.jobPrize.companyService.service.Application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.service.Application.api.ApplicationActionService;
import com.jobPrize.companyService.service.integrated.InterestService;
import com.jobPrize.entity.memToCom.Application;
import com.jobPrize.entity.memToCom.Pass;
import com.jobPrize.repository.memToCom.application.ApplicationRepository;
import com.jobPrize.repository.memToCom.pass.PassRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultApplicationActionService implements ApplicationActionService {

    private final ApplicationRepository applicationRepository;
    private final InterestService interestService;
    private final PassRepository passRepository;

    @Override
    @Transactional
    public void toggleInterestForApplication(Long companyId, Long applicationId) {
        interestService.toggleInterestForApplication(companyId, applicationId);
    }

    @Override
    @Transactional
    public void markAsPassed(Long applicationId) {
        Application application = applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new IllegalStateException("지원서를 찾을 수 없습니다."));
        Pass pass = passRepository.findByApplication(application);

        if (pass == null) {
            pass = new Pass(application);
        }

        pass.markAsPassed();
        passRepository.save(pass);
    }
}