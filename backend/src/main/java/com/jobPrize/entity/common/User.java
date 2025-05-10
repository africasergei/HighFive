package com.jobPrize.entity.common;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jobPrize.entity.admin.Admin;
import com.jobPrize.entity.company.Company;
import com.jobPrize.entity.consultant.Consultant;
import com.jobPrize.entity.dummy.Chat;
import com.jobPrize.entity.dummy.Payment;
import com.jobPrize.entity.dummy.Subscription;
import com.jobPrize.entity.member.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	private String password;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String phone;

	@Column(nullable = false)
	private String address;

	@Builder.Default
	@Column(nullable = false, name = "IS_SUBSCRIBED")
	private boolean isSubscribed = false;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserType type;

	@CreatedDate
	@Column(nullable = false, name = "CREATED_DATE")
	private LocalDate createdDate;

	@Column(name = "DELETED_DATE")
	private LocalDate deletedDate;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Member member;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Company company;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Consultant consultant;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Admin admin;
	
	@BatchSize(size = 10)
	@OneToMany(mappedBy = "user")
	private List<Payment> payments = new ArrayList<>();
	
	@OneToMany(mappedBy = "user")
	private List<Chat> chats = new ArrayList<>();
	
	@OneToMany(mappedBy = "user")
	private List<Subscription> Subscriptions = new ArrayList<>();

	
	
	public void updatePassword(String password) {
		this.password = password;
	}

	public void updatePhone(String phone) {
		this.phone = phone;
	}

	public void updateAddress(String address) {
		this.address = address;
	}

	public void deleteUser() {
		this.deletedDate = LocalDate.now();
	}

	public void subscribe() {
		this.isSubscribed = true;
	}

	public void unSubscribe() {
		this.isSubscribed = false;
	}

}
