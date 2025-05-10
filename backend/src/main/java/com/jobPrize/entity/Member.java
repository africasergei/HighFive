package com.jobPrize.entity;

import java.util.List;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
	
	@Id
	@Column(name="USER_ID", nullable = false)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private User user;
	
    @Column(nullable = false)
	private String nickname;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Resume resume;
	
	@OneToMany(mappedBy = "member")
	private List<CoverLetter> coverLetters;
	
	@OneToMany(mappedBy = "member")
	private List<CareerDescription> careerDescriptions;
	
	@BatchSize(size = 10)
	@OneToMany(mappedBy = "member")
	private List<Application> applications;
	
	@BatchSize(size = 10)
	@OneToMany(mappedBy = "member")
	private List<Proposal> proposals;
	
	@BatchSize(size = 10)
	@OneToMany(mappedBy = "member")
	private List<RequestDocument> requestDocuments;
//	
//	@OneToMany(mappedBy = "member")
//	private List<Interest> interests;
//	
//	@OneToMany(mappedBy = "member")
//	private List<Similarity> similarities;
//	

	

    public void updateNickname(String nickname) {
    	this.nickname=nickname;
    }

}
