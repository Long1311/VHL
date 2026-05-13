package com.vhl.htqt.organization.repository;

import com.vhl.htqt.organization.entity.OrganizationMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, Long> {

    Page<OrganizationMember> findByOrganizationIdAndIsDeletedFalseOrderByIdDesc(
            Long organizationId,
            Pageable pageable
    );

    Page<OrganizationMember> findByOrganizationIdAndIsDeletedFalseAndFullNameContainingIgnoreCaseOrOrganizationIdAndIsDeletedFalseAndEmailContainingIgnoreCaseOrderByIdDesc(
            Long organizationIdForFullName,
            String fullNameKeyword,
            Long organizationIdForEmail,
            String emailKeyword,
            Pageable pageable
    );

    Optional<OrganizationMember> findByIdAndIsDeletedFalse(Long id);

    Optional<OrganizationMember> findByIdAndOrganizationIdAndIsDeletedFalse(Long id, Long organizationId);

    //Kiểm tra tổ chức hiện tại còn member nào chưa bị xóa không.
    boolean existsByOrganizationIdAndIsDeletedFalse(Long organizationId);
}