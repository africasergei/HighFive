package com.jobPrize.repository.company.company;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobPrize.entity.company.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
	Optional<Company> findByUserId(Long id);

}