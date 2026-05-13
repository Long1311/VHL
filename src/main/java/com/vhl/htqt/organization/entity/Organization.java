package com.vhl.htqt.organization.entity;

import com.vhl.htqt.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "organizations",
        indexes = {
                @Index(name = "idx_organizations_type", columnList = "type"),
                @Index(name = "idx_organizations_parent_id", columnList = "parent_id"),
                @Index(name = "idx_organizations_is_deleted", columnList = "is_deleted"),
                @Index(name = "idx_organizations_name", columnList = "name")
        }
)
public class Organization extends BaseEntity {
    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrganizationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Organization parent;

    @Column(length = 255)
    private String region;

    @Column(length = 255)
    private String country;

    @Column(length = 500)
    private String address;

    @Column(length = 255)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(length = 255)
    private String website;

    @Column(name = "contact_person", length = 255)
    private String contactPerson;

    @Column(columnDefinition = "TEXT")
    private String description;
}
