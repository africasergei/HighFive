package com.jobPrize.service.memToCon.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jobPrize.dto.memToCon.request.RequestCreateDto;
import com.jobPrize.dto.memToCon.request.RequestDetailDto;
import com.jobPrize.dto.memToCon.request.RequestSummaryDto;
import com.jobPrize.enumerate.UserType;

public interface RequestService {
	void createRequest(Long id, UserType userType, RequestCreateDto requestCreateDto);
	Page<RequestSummaryDto> readFeedbackRequestPage(Long id, Pageable pageable);
	Page<RequestSummaryDto> readEditRequestPage(Long id, Pageable pageable);
	RequestDetailDto readRequestDetail(Long id, UserType userType, Long requestId);
	void createRequestToConsultant(Long id, boolean isSubscribed, Long requestId);
	
}
