package com.vhl.htqt.organization.dto;

import com.vhl.htqt.organization.entity.OrganizationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrganizationResponse {
    private Long id;

    private String code;

    private String name;

    private OrganizationType type;

    private Long parentId;

    private String parentName;

    private String region;

    private String country;

    private String address;

    private String email;

    private String phone;

    private String website;

    private String contactPerson;

    private String description;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
