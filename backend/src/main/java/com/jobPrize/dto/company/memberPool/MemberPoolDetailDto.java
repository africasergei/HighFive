package com.jobPrize.dto.company.memberPool;

import java.time.LocalDate;
import java.util.List;

import com.jobPrize.dto.member.career.CareerResponseDto;
import com.jobPrize.dto.member.certification.CertificationResponseDto;
import com.jobPrize.dto.member.education.EducationResponseDto;
import com.jobPrize.dto.member.languageTest.LanguageTestResponseDto;
import com.jobPrize.entity.common.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberPoolDetailDto {
	
	private String name;
	
	private String email;
	
	private String genderType;
	
	private LocalDate birthDate;
	
	private boolean hasCareer;
	
	private String job;
	
	private String phone;

	private EducationResponseDto educationResponseDto;
	
	private CareerResponseDto  careerResponseDto;
	
	private List<CertificationResponseDto> certificationResponseDto;
	
	private List<LanguageTestResponseDto> languageTestResponseDto;

	public static MemberPoolDetailDto of(
			User user, 
			boolean hasCareer, 
			String job, 
			EducationResponseDto educationResponseDto,
			CareerResponseDto  careerResponseDto,
			List<CertificationResponseDto> certificationResponseDto,
			List<LanguageTestResponseDto> languageTestResponseDto) {
		
		return MemberPoolDetailDto.builder()
			.name(user.getName())
			.email(user.getEmail())
			.genderType(user.getGenderType().name())
			.birthDate(user.getBirthDate())
			.hasCareer(hasCareer)
			.job(job)
			.phone(user.getPhone())
			.educationResponseDto(educationResponseDto)
			.careerResponseDto(careerResponseDto)
			.certificationResponseDto(certificationResponseDto)
			.languageTestResponseDto(languageTestResponseDto)
			.build();
	}
}