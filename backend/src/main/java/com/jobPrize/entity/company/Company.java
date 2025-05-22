package com.jobPrize.entity.company;



import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import com.jobPrize.entity.common.User;
import com.jobPrize.entity.memToCom.Interest;
import com.jobPrize.entity.memToCom.Proposal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Entity
@Table(name = "company")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

	@Id
	@Column(name="USER_ID", nullable = false)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name="USER_ID", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "industry", nullable = false) //DB와 반대구조 업종클래스에서도 관계설정
	private Industry industry;
	
	@Column(name = "company_name", nullable = false)
	private String companyName;

	@Column(name = "representative_name", nullable = false)
	private String representativeName;
	
	@Column(name = "business_number", nullable = false)
	private String businessNumber;

	@Column(name = "company_adress", nullable = false)
	private String companyAddress;

	@Column(name = "company_phone", nullable = false)
	private String companyPhone;
	
	@Column(name = "company_email", nullable = false)
	private String companyEmail;
	
	@Column(name = "introduction")
	private String introduction;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private CompanyType type;

	@Column(name = "employee_count")
	private int employeeCount;

	@Column(name = "established_date", nullable = false)
	private LocalDate establishedDate;

	@OneToMany(mappedBy = "company")
	private List<Schedule> schedules = new ArrayList<>();
	
	@OneToMany(mappedBy = "company")
	private List<Advertisement> advertisements = new ArrayList<>();
	
	@OneToMany(mappedBy = "company")
	private List<JobPosting> jobPostings = new ArrayList<>();

	@OneToMany(mappedBy = "company")
	private List<Proposal> proposals = new ArrayList<>();
	
	@OneToMany(mappedBy = "company")
	private List<Interest> interests = new ArrayList<>();
	
	public void updateCompanyInfo(String companyName, String representativeName, LocalDate establishedDate, String businessNumber,
			String companyAddress, CompanyType type, Industry industry, String companyPhone, String introduction,
			int employeeCount) {
		this.companyName = companyName;
		this.representativeName = representativeName;
		this.establishedDate = establishedDate;
		this.businessNumber = businessNumber;
		this.companyAddress = companyAddress;
		this.type = type;
		this.industry = industry;
		this.companyPhone = companyPhone;
		this.introduction = introduction;
		this.employeeCount = employeeCount;

	}
	

	
}
