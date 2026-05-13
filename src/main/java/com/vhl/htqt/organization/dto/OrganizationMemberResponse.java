package com.vhl.htqt.organization.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrganizationMemberResponse {

    private Long id;

    private Long organizationId;

    private String organizationName;

    private String fullName;

    private String position;

    private String email;

    private String phone;

    private String description;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
