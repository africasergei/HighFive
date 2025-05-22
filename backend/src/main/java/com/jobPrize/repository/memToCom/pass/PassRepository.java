package com.jobPrize.repository.memToCom.pass;


import org.springframework.data.jpa.repository.JpaRepository;

import com.jobPrize.entity.memToCom.Application;
import com.jobPrize.entity.memToCom.Pass;

public interface PassRepository extends JpaRepository<Pass, Long>,PassRepositoryCustom {
	boolean existsByIdAndIsPassedTrue(Long applicationId);
	Pass findByApplication(Application application);
}
