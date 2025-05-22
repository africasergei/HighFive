package com.jobPrize.companyService.service.proposal.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobPrize.companyService.dto.proposal.ProposalResponseListDto;
import com.jobPrize.repository.memToCom.proposal.ProposalRepository;
import com.jobPrize.companyService.service.proposal.api.ProposalSearchService;
import lombok.RequiredArgsConstructor;
import com.jobPrize.entity.memToCom.Proposal;

@Service
@RequiredArgsConstructor
public class DefaultProposalSearchService implements ProposalSearchService {

    private final ProposalRepository proposalRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProposalResponseListDto> readProposalsByCompanyId(Long companyId, Pageable pageable) {
        Page<Proposal> proposalPage = proposalRepository.findAllByCompanyId(companyId, pageable);
        return proposalPage.map(this::convertToDto);
    }

    private ProposalResponseListDto convertToDto(Proposal proposal) {
        return ProposalResponseListDto.builder()
                .id(proposal.getId())
                .name(proposal.getMember().getUser().getName())
                .genderType(proposal.getMember().getUser().getGenderType())
                .birthDate(proposal.getMember().getUser().getBirthDate())
                .hasCareer(!proposal.getMember().getCareers().isEmpty())
                .job(proposal.getProposalJob())
                .educationLevel(!proposal.getMember().getEducations().isEmpty() ? proposal.getMember().getEducations().get(0).getEducationLevel() : null)
                .proposalDate(proposal.getProposalDate())
                .proposalStatus(proposal.getProposalStatus())
                .build();
    }
}