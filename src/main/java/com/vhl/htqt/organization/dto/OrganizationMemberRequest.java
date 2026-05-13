package com.vhl.htqt.organization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationMemberRequest {

    @NotBlank(message = "Họ tên thành viên không được để trống")
    @Size(max = 255, message = "Họ tên không được vượt quá 255 ký tự")
    private String fullName;

    @Size(max = 255, message = "Chức vụ không được vượt quá 255 ký tự")
    private String position;

    @Email(message = "Email không đúng định dạng")
    @Size(max = 255, message = "Email không được vượt quá 255 ký tự")
    private String email;

    @Size(max = 50, message = "Số điện thoại không được vượt quá 50 ký tự")
    private String phone;

    private String description;

    private Boolean isActive;
}
