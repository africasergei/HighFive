package com.jobPrize.companyService.dto;

import com.jobPrize.entity.memToCom.EducationLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFilterCondition {
    // 검색 조건
    private String searchKeyword;  // 검색어
    private String searchType;     // 검색 타입 (name, job, company 등)
    
    // 필터 조건
    private boolean hasCareer;        // 경력 유무
    private EducationLevel education; // 최소 학력
    private String address;          // 근무지
    private String job;

    // 관심 필터
    private boolean favoriteOnly;    // 관심만 보기 여부
    
    // 정렬 조건
    private String sortBy;           // 정렬 기준 (similarity, favoriteCount 등)
    private boolean isDescending;    // 내림차순 여부
}
