package com.jobPrize.dto.company.jobPosting;

import java.util.List;

import com.jobPrize.entity.memToCom.EducationLevel;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class JobPostingCreateDto {
	
	@NotBlank(message = "공고명을 입력해주세요.")
	@Size(max = 50, message = "공고명은 최대 50자까지 입력 가능합니다.")
	private String title;

	@NotBlank(message = "근무 시간을 입력해주세요.")
	@Size(max = 20, message = "근무 시간은 최대 20자까지 입력 가능합니다.")
	private String workingHours;

	@NotBlank(message = "근무지를 입력해주세요.")
	@Size(max = 10, message = "근무지는 최대 10자까지 입력 가능합니다.")
	private String workLocation;

	@NotBlank(message = "모집 부문을 입력해주세요.")
	@Size(max = 20, message = "모집 부문은 최대 20자까지 입력 가능합니다.")
	private String job; // 모집 부문을 job 필드로 사용

	@NotBlank(message = "경력 조건을 입력해주세요.")
	@Size(max = 10, message = "경력 조건은 최대 10자까지 입력 가능합니다.")
	private String careerType;

	@NotNull(message = "요구 학력을 선택해주세요.")
	private EducationLevel educationLevel;

	@Min(value = 0, message = "급여는 0 이상이어야 합니다.")
	@Max(value = 1000000000, message = "급여는 10억 이하이어야 합니다.")
	private int salary;

	@Size(max = 500, message = "내용은 최대 500자까지 입력 가능합니다.")
	private String content;

	@NotBlank(message = "자격 요건을 입력해주세요.")
	@Size(min = 50, message = "자격 요건은 최소 50자 이상 입력해야 합니다.")
	private String requirement;

}