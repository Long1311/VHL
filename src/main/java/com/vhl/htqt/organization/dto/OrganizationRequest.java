package com.vhl.htqt.organization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationRequest {
    @NotBlank(message = "Mã tổ chức không được để trống")
    @Size(max = 50, message = "Mã tổ chức không được vượt quá 50 ký tự")
    private String code;

    @NotBlank(message = "Tên tổ chức không được để trống")
    @Size(max = 255, message = "Tên tổ chức không được vượt quá 255 ký tự")
    private String name;

    private Long parentId;

    @Size(max = 255, message = "Khu vực không được vượt quá 255 ký tự")
    private String region;

    @Size(max = 255, message = "Quốc gia không được vượt quá 255 ký tự")
    private String country;

    @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
    private String address;

    @Email(message = "Email không đúng định dạng")
    @Size(max = 255, message = "Email không được vượt quá 255 ký tự")
    private String email;

    @Size(max = 50, message = "Số điện thoại không được vượt quá 50 ký tự")
    private String phone;

    @Size(max = 255, message = "Website không được vượt quá 255 ký tự")
    private String website;

    @Size(max = 255, message = "Người đầu mối không được vượt quá 255 ký tự")
    private String contactPerson;

    private String description;

    private Boolean isActive;
}
