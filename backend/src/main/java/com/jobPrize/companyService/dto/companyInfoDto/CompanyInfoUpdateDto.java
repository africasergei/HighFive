package com.jobPrize.companyService.dto.companyInfoDto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyInfoUpdateDto {

    @NotBlank(message = "기업명은 필수 항목입니다.")
    @Size(max = 20, message = "기업명은 최대 20자까지 입력 가능합니다.")
    private String companyName;

    @NotBlank(message = "대표자명은 필수 항목입니다.")
    @Size(max = 10, message = "대표자명은 최대 10자까지 입력 가능합니다.")
    private String ceorepresentativeName;

    @NotBlank(message = "사업자등록번호는 필수 항목입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "사업자등록번호 형식이 올바르지 않습니다. (예: 123-45-67890)")
    private String businessNumber;

    @NotNull(message = "설립일은 필수 항목입니다.")
    private LocalDate incorporationDate;

    @NotNull(message = "직원수는 필수 항목입니다.")
    private Integer employeeCount;

    @NotBlank(message = "업종은 필수 항목입니다.")
    @Size(max = 20, message = "업종은 최대 20자까지 입력 가능합니다.")
    private String industry;

    @NotBlank(message = "기업주소는 필수 항목입니다.")
    @Size(max = 50, message = "주소는 최대 50자까지 입력 가능합니다.")
    private String Companyaddress;

    @NotBlank(message = "기업 전화번호는 필수 항목입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. (예: 02-1234-5678 또는 010-1234-5678)")
    private String companyPhone;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Size(max = 30, message = "이메일은 최대 30자까지 입력 가능합니다.")
    private String Email;

    @Size(max = 200, message = "기업 소개는 최대 200자까지 입력 가능합니다.")
    private String Introduction;
    
    @NotBlank(message = "담당자명은 필수 항목입니다.")
    @Size(max = 10, message = "담당자명은 최대 10자까지 입력 가능합니다.")
    private String name;

    @NotBlank(message = "연락처는 필수 항목입니다.")
    @Size(min = 11, max = 11, message = "연락처는 11자리로 입력해야 합니다.")
    private String phone;

    @NotBlank(message = "주소는 필수 항목입니다.")
    @Size(max = 50, message = "주소는 최대 50자까지 입력 가능합니다.")
    private String address;

   }