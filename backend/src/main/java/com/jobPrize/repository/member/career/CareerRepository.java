package com.jobPrize.repository.member.career;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobPrize.entity.member.Career;

<<<<<<< HEAD
public interface CareerRepository extends JpaRepository<Career, Long>, CareerRepositoryCustom {
=======
public interface CareerRepository extends JpaRepository<Career, Long>, CareerRepositoryCostom {
	 Optional<Career> findTopByMemberIdOrderByStartDateDesc(Long memberId);
>>>>>>> 8ab16768bc3dd30e2fa438780aed4e324b7c1e1a

}
