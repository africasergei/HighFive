package com.jobPrize.service.admin.feedbackPrompt;

import java.util.List;

import com.jobPrize.dto.admin.feedbackPrompt.FeedbackPromptCreateDto;
import com.jobPrize.dto.admin.feedbackPrompt.FeedbackPromptResponseDto;
import com.jobPrize.dto.admin.feedbackPrompt.FeedbackPromptUpdateDto;
import com.jobPrize.entity.common.UserType;

public interface FeedbackPromptService {

	void createFeedbackPrompt(UserType userType, FeedbackPromptCreateDto dto);

	void updateFeedbackPrompt(UserType userType, FeedbackPromptUpdateDto dto );

	List<FeedbackPromptResponseDto> readAllList();

	FeedbackPromptResponseDto readFeedbackPromptById(FeedbackPromptResponseDto dto);

	void applyFeedbackPrompt(Long feedbackPromptId);

	void unApplyFeedbackPrompt();

}
