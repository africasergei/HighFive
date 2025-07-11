package com.jobPrize.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobPrize.dto.common.token.TokenDto;
import com.jobPrize.dto.common.user.kakao.KakaoAccessTokenDto;
import com.jobPrize.dto.common.user.login.LogInDto;
import com.jobPrize.service.common.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;

	@PostMapping("/token")
	public ResponseEntity<TokenDto> logIn(@RequestBody @Valid LogInDto logInDto) {

		TokenDto tokenDto = userService.logIn(logInDto);

		return ResponseEntity.status(HttpStatus.OK).body(tokenDto);

	}

	@PostMapping("/kakao")
	public ResponseEntity<?> loginKakao(@RequestBody KakaoAccessTokenDto kakaoAccessTokenDto) {
		String kakaoAccessToken = kakaoAccessTokenDto.getKakaoAccessToken();
		
		String email = userService.getEmailFromKakaoAccessToken(kakaoAccessToken);
		
		if(userService.isExistEmail(email)) {
			TokenDto tokenDto = userService.logInForKakao(email);
			return ResponseEntity.status(HttpStatus.OK).body(tokenDto);
		}

		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(email);
		

	}

}
