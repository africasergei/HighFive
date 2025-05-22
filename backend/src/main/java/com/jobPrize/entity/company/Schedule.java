package com.jobPrize.entity.company;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

import com.jobPrize.entity.admin.Admin;
import com.jobPrize.entity.common.User;
@Getter
@Entity
@Table(name = "schedule")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "schedule_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private Company company;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content")
	private String content;
	
	@Column(name = "date")
	private LocalDate date;

	public void updateSchedule(String title, String content,LocalDate date) {
		this.title = title;
		this.content = content;
		this.date = date;
	}
	
	
}