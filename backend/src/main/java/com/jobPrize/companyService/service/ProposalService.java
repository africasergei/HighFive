package com.jobPrize.companyService.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobPrize.companyService.dto.proposal.ProposalRequestCreateDto;
import com.jobPrize.companyService.dto.proposal.ProposalResponseListDto;
import com.jobPrize.entity.memToCom.Proposal;
import com.jobPrize.entity.member.Education;
import com.jobPrize.entity.member.Member;
import com.jobPrize.jwt.TokenProvider;
import com.jobPrize.repository.common.UserRepository;
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

    @Transactional
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

    @Transactional(readOnly = true)
    public Page<ProposalResponseListDto> readProposalsByCompanyId(Long companyId, Pageable pageable) {
        Page<Proposal> proposalPage = proposalRepository.findAllByCompanyId(companyId, pageable);
        
        return proposalPage.map(this::convertToDto);
    }

    private ProposalResponseListDto convertToDto(Proposal proposal) {
        Member member = proposal.getMember(); // ✅ member 직접 가져오기
        List<Education> educations = member.getEducations(); // ✅ educations 리스트 가져오기

        return ProposalResponseListDto.builder()
        		.id(proposal.getId())
                .name(member.getUser().getName()) // ✅ null 처리 제거
                .gender(member.getUser().getGenderType()) // ✅ null 처리 제거
                .age(member.getUser().getAge()) // ✅ 기본값 설정 필요 없음
                .hasCareer(!member.getCareers().isEmpty())
                .job(proposal.getProposalJob())
                .educationLevel(educations.get(0).getEducationLevel()) // ✅ 항상 존재한다고 가정
                .proposalDate(proposal.getProposalDate())
                .proposalStatus(proposal.getProposalStatus())
                .build();
    }

    
    private Long getUserIdFromToken(String token) {
        return Long.parseLong(tokenProvider.getIdFromToken(token));
    }
}