package com.jobPrize.companyService.dto.schedule;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleUpdateRequestDto {
	private Long scheduleId;
	private Long companyId;

	@NotBlank
	@Size(max = 30, message = "제목을 최대 30자이내로 입력하세요")
	private String title;

	@NotBlank
	@Size(max = 100, message = "내용을 최대 100자이내로 입력하세요")
	private String content;

	@NotNull(message = "날짜를 선택해주세요.")
	private LocalDate date;

}
