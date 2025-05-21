package com.jobPrize.entity.company;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jobPrize.entity.memToCom.Application;
import com.jobPrize.entity.memToCom.EducationLevel;
import com.jobPrize.entity.memToCom.Similarity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Entity
@Table(name = "job_posting")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class JobPosting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_posting_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private Company company;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content")
	private String content;

	@Column(name = "job", nullable = false)
	private String job;

	@Column(name = "requirement")
	private String requirement;

	@Column(name = "working_hours", nullable = false)
	private String workingHours;

	@Column(name = "work_location", nullable = false)
	private String workLocation;

	@Column(name = "career_type", nullable = false)
	private String careerType;

	@Enumerated(EnumType.STRING)
	@Column(name = "education_level", nullable = false)
	private EducationLevel educationLevel;

	@Column(name = "salary")
	private int salary;

	@CreatedDate
	@Column(name = "created_date", nullable = false)
	private LocalDate createdDate;

	@Column(name = "expired_date", nullable = false)
	private LocalDate expiredDate;
	
	@Enumerated(EnumType.STRING) 
    @Column(name = "company_type", nullable = false)
    private CompanyType companyType; 

	@OneToMany(mappedBy = "jobPosting",cascade = CascadeType.ALL)
	private List<JobPostingImage> jobPostingImages = new ArrayList<>();

	@OneToMany(mappedBy = "jobPosting")
	private List<Application> applications = new ArrayList<>();
	
	@OneToMany(mappedBy = "jobPosting")
	private List<Similarity> similarities = new ArrayList<>();

	public void updateJobPostingInfo(String title, String content, String job, String workingHours, String workLocation,
			String careerType, EducationLevel educationLevel, int salary, String requirement) {
		this.title = title;
		this.content = content;
		this.job = job;
		this.workingHours = workingHours;
		this.workLocation = workLocation;
		this.careerType = careerType;
		this.educationLevel = educationLevel;
		this.salary = salary;
		this.requirement = requirement;
		this.companyType = companyType;
	}


}
