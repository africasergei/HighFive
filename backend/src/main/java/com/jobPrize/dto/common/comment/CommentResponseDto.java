package com.jobPrize.dto.common.comment;

import java.time.LocalDateTime;

import com.jobPrize.entity.common.Comment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
	private Long authorId;
	private String nicknameOrName;
	private String content;
	private LocalDateTime createdDate;
	
	public static CommentResponseDto of(Comment comment, String nicknameOrName) {
		return CommentResponseDto
				.builder()
				.authorId(comment.getUser().getId())
				.nicknameOrName(nicknameOrName)
				.content(comment.getContent())
				.createdDate(comment.getCreatedTime())
				.build();
	}

}
