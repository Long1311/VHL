package com.vhl.htqt.organization.entity;

import com.vhl.htqt.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "organization_members",
        indexes = {
                @Index(name = "idx_members_organization_id", columnList = "organization_id"),
                @Index(name = "idx_members_is_deleted", columnList = "is_deleted"),
                @Index(name = "idx_members_full_name", columnList = "full_name")
        }
)
public class OrganizationMember extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(length = 255)
    private String position;

    @Column(length = 255)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String description;
}
