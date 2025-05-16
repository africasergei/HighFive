package com.jobPrize.companyService.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MembePoolrDto {
    private Long memberId;         
    private String name;          
    private String job;       
    private boolean hasCareer; 
    private int similarityScore;   
    private Long jobPostingId;     
}
