package com.jobPrize.companyService.service.integrated;

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
import com.jobPrize.repository.memToCom.proposal.ProposalRepository;
import com.jobPrize.repository.member.member.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Proposal createProposal(Long userId, ProposalRequestCreateDto dto) {
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
        Member member = proposal.getMember();
        List<Education> educations = member.getEducations();

        return ProposalResponseListDto.builder()
                .id(proposal.getId())
                .name(member.getUser().getName()) 
                .genderType(member.getUser().getGenderType()) 
                .birthDate(member.getUser().getBirthDate()) 
                .hasCareer(!member.getCareers().isEmpty())
                .job(proposal.getProposalJob())
                .educationLevel(!educations.isEmpty() ? educations.get(0).getEducationLevel() : null) 
                .proposalDate(proposal.getProposalDate())
                .proposalStatus(proposal.getProposalStatus())
                .build();
    }
}