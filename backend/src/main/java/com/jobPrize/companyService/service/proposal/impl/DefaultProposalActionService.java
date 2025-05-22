package com.jobPrize.companyService.service.proposal.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobPrize.companyService.dto.proposal.ProposalRequestCreateDto;
import com.jobPrize.entity.memToCom.Proposal;
import com.jobPrize.entity.member.Member;
import com.jobPrize.repository.memToCom.proposal.ProposalRepository;
import com.jobPrize.repository.member.member.MemberRepository;
import com.jobPrize.companyService.service.proposal.api.ProposalActionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultProposalActionService implements ProposalActionService {

    private final ProposalRepository proposalRepository;
    private final MemberRepository memberRepository;

    @Override
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
}