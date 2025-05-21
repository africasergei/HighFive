package com.jobPrize.companyService.dto.schedule;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleResponseDto {
private Long scheduleId;
private Long companyId;
private String title;
private LocalDate date;

}
