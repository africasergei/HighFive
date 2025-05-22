package com.jobPrize.entity.memToCom;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "pass")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Pass {

    @Id
    @Column(name = "pass_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;
    
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "is_passed", nullable = false) // ✅ 합격 여부 필드 추가
    private boolean isPassed;

    public void pass() { 
        this.isPassed = true; // ✅ 상태 변경
    }

    public boolean isPassed() {
        return isPassed; // ✅ 현재 합격 여부 조회
    }
    public Pass(Application application) {
        this.application = application;
        this.isPassed = false; // 기본값 설정
        this.createdDate = LocalDate.now(); // ✅ 합격 날짜 저장
    }
    public void markAsPassed() {  
        this.isPassed = true; // ✅ 합격 상태 변경
        this.createdDate = LocalDate.now(); // ✅ 합격 날짜 기록
    }

}