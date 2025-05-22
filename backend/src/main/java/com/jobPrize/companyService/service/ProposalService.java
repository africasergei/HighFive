package com.jobPrize.companyService.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobPrize.entity.common.GenderType;
import com.jobPrize.companyService.dto.proposal.ProposalRequestCreateDto;
import com.jobPrize.companyService.dto.proposal.ProposalResponseListDto;
import com.jobPrize.entity.memToCom.Proposal;
import com.jobPrize.entity.member.Education;
import com.jobPrize.entity.member.Member;
import com.jobPrize.jwt.TokenProvider;
import com.jobPrize.repository.common.user.UserRepository;
import com.jobPrize.repository.memToCom.proposal.ProposalRepository;
import com.jobPrize.repository.member.member.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Transactional // ✅ 제안 생성
    public Proposal createProposal(String token, ProposalRequestCreateDto dto) {
        Long userId = getUserIdFromToken(token);

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자 정보를 찾을 수 없습니다."));

        Proposal proposal = Proposal.builder()
                .proposalTitle(dto.getProposalTitle())
                .member(member)
                .companyName(dto.getCompanyName())
                .proposalContent(dto.getProposalContent())
                .proposalJob(dto.getProposalJob())
                .proposalSalary(dto.getProposalSalary())
                .build();

        return proposalRepository.save(proposal);
    }

    @Transactional(readOnly = true) // ✅ 기업 기준 제안 목록 조회
    public Page<ProposalResponseListDto> readProposalsByCompanyId(String token, Pageable pageable) {
        Long companyId = getCompanyIdFromToken(token);
        Page<Proposal> proposalPage = proposalRepository.findAllByCompanyId(companyId, pageable);

        return proposalPage.map(this::convertToDto);
    }

    // 🔹 DTO 변환 로직
    private ProposalResponseListDto convertToDto(Proposal proposal) {
        Member member = proposal.getMember();
        List<Education> educations = member.getEducations();

        return ProposalResponseListDto.builder()
                .id(proposal.getId())
                .name(member.getUser().getName()) 
                .genderType(member.getUser().getGenderType()) 
                .birthDate(member.getUser().getBirthDate()) // ✅ 기본값 설정 필요 없음
                .hasCareer(!member.getCareers().isEmpty())
                .job(proposal.getProposalJob())
                .educationLevel(!educations.isEmpty() ? educations.get(0).getEducationLevel() : null) // ✅ 빈 리스트 예외 처리
                .proposalDate(proposal.getProposalDate())
                .proposalStatus(proposal.getProposalStatus())
                .build();
    }

    // 🔹 안정적인 사용자 ID 가져오기
    private Long getUserIdFromToken(String token) {
        try {
            return tokenProvider.getIdFromToken(token);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("토큰에서 유효한 사용자 ID를 찾을 수 없습니다.", e);
        }
    }

    // 🔹 안정적인 기업 ID 가져오기
    private Long getCompanyIdFromToken(String token) {
        Long userId = getUserIdFromToken(token);
        return userRepository.findById(userId)
                .map(user -> user.getCompany().getId())
                .orElseThrow(() -> new IllegalStateException("사용자의 기업 정보를 찾을 수 없습니다."));
    }
}