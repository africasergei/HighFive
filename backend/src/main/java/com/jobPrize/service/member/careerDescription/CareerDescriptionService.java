package com.jobPrize.service.member.careerDescription;

import java.util.List;

import com.jobPrize.dto.member.careerDescription.CareerDescriptionCreateDto;
import com.jobPrize.dto.member.careerDescription.CareerDescriptionResponseDto;
import com.jobPrize.dto.member.careerDescription.CareerDescriptionSummaryDto;
import com.jobPrize.dto.member.careerDescription.CareerDescriptionUpdateDto;
import com.jobPrize.enumerate.UserType;

public interface CareerDescriptionService {

    CareerDescriptionResponseDto createCareerDescription(Long id, UserType userType, CareerDescriptionCreateDto careerDescriptionCreateDto);

    List<CareerDescriptionSummaryDto> readCareerDescriptionList(Long id);

    CareerDescriptionResponseDto readCareerDescription(Long id, Long careerDescriptionId);

    CareerDescriptionResponseDto updateCareerDescription(Long id, CareerDescriptionUpdateDto careerDescriptionUpdateDto);
    
    void deleteCareerDescription(Long id, Long careerDescriptionId);

}
