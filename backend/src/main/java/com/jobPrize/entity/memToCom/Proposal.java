package com.jobPrize.entity.memToCom;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jobPrize.entity.company.Company;
import com.jobPrize.entity.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Entity
@Table(name = "proposal")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Proposal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "proposal_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id", nullable = false)
	private Company company;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "proposal_title", nullable = false)
	private String proposalTitle;
	
	@Column(name = "proposal_campanyName",nullable = false)
	private String companyName;

	@Column(name = "proposal_content", nullable = false)
	private String proposalContent;

	@Column(name = "proposal_salary", nullable = false)
	private int proposalSalary;

	@Column(name = "proposal_job", nullable = false)
	private String proposalJob;
	
	@CreatedDate
	@Column(name = "proposal_date", nullable = false)
	private LocalDate proposalDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "proposal_status", nullable = false)
	private ProposalStatus proposalStatus;


	
	public void changeStatus(ProposalStatus status) {
		this.proposalStatus = status;
	}



	
}