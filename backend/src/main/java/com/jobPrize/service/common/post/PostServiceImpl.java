package com.jobPrize.service.common.post;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.customException.CustomEntityNotFoundException;
import com.jobPrize.dto.common.comment.CommentResponseDto;
import com.jobPrize.dto.common.post.PostCreateDto;
import com.jobPrize.dto.common.post.PostResponseDto;
import com.jobPrize.dto.common.post.PostSummaryDto;
import com.jobPrize.dto.common.post.PostUpdateDto;
import com.jobPrize.entity.common.Comment;
import com.jobPrize.entity.common.Post;
import com.jobPrize.entity.common.User;
import com.jobPrize.repository.common.post.PostRepository;
import com.jobPrize.repository.common.user.UserRepository;
import com.jobPrize.util.AssertUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;

	private final UserRepository userRepository;

	private final AssertUtil assertUtil;
	
	private static final String ENTITY_NAME = "게시글";

	@Override
	public void createPost(Long id, PostCreateDto dto) {

		User user = userRepository.findByIdAndDeletedDateIsNull(id)
		            .orElseThrow(() -> new CustomEntityNotFoundException("유저"));
		
		Post post = Post.of(user,dto);
		postRepository.save(post);
	}

	@Override
	public void updatePost(Long id, PostUpdateDto dto) {
		
		String action = "수정";

		Post post = postRepository.findById(dto.getId())
				.orElseThrow(() -> new CustomEntityNotFoundException(ENTITY_NAME));
		
		Long ownerId = postRepository.findUserIdByPostId(dto.getId())
				.orElseThrow(() -> new CustomEntityNotFoundException("소유자"));
		
		assertUtil.assertId(id, ownerId, ENTITY_NAME, action);
	
		post.updatePost(dto.getTitle(), dto.getContent());

	}

	@Override
	@Transactional(readOnly = true)
	public Page<PostSummaryDto> readPostPage(Pageable pageable) {

	    Page<Post> postPage = postRepository.findAll(pageable);

	    List<Post> postList = postPage.getContent();
	    List<PostSummaryDto> dtoList = new ArrayList<>();
		
	    for(Post post : postList) {
	    	dtoList.add(PostSummaryDto.of(post, getNicknameOrNameFromPost(post)));
	    }
	    return new PageImpl<PostSummaryDto>(dtoList, pageable, postPage.getTotalElements());
	}

	
	@Override
	@Transactional(readOnly = true)
	public PostResponseDto readPost(Long postId) {
		
	    Post post = postRepository.findWithCommentsByPostId(postId)
	        .orElseThrow(() -> new CustomEntityNotFoundException(ENTITY_NAME));
	    
	    List<Comment> comments = post.getComments();
	    List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
	    for(Comment comment : comments) {

	    	commentResponseDtos.add(CommentResponseDto.of(comment, getNicknameOrNameFromComment(comment)));
	    }
	    return PostResponseDto.of(post,getNicknameOrNameFromPost(post),commentResponseDtos);
	}
	
	@Override
	public void deletePost(Long id, Long postId) {
		
		String action = "삭제";
		
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new CustomEntityNotFoundException(ENTITY_NAME));
		
		Long ownerId = postRepository.findUserIdByPostId(postId)
				.orElseThrow(() -> new CustomEntityNotFoundException("소유자"));
		
		assertUtil.assertId(id, ownerId, ENTITY_NAME, action);
		
		postRepository.delete(post);
		
	}
	
	
	
	private String getNicknameOrNameFromPost(Post post) {
		String result;
		
		if(post.getUser().getMember()==null) {
			result = post.getUser().getName();
	    }
	    else {
	    	result = post.getUser().getMember().getNickname();
	    }
		
		return result;
	}
	
	private String getNicknameOrNameFromComment(Comment comment) {
		String result;
		
		if(comment.getUser().getMember()==null) {
			result = comment.getUser().getName();
	    }
	    else {
	    	result = comment.getUser().getMember().getNickname();
	    }
		
		return result;
	}
	
	
	
	

	



}
