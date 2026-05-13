package com.vhl.htqt.organization.repository;

import com.vhl.htqt.organization.entity.Organization;
import com.vhl.htqt.organization.entity.OrganizationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Page<Organization> findByTypeAndIsDeletedFalseOrderByIdDesc(
            OrganizationType type,
            Pageable pageable
    );

    Page<Organization> findByTypeAndIsDeletedFalseAndCodeContainingIgnoreCaseOrTypeAndIsDeletedFalseAndNameContainingIgnoreCaseOrderByIdDesc(
            OrganizationType typeForCode,
            String codeKeyword,
            OrganizationType typeForName,
            String nameKeyword,
            Pageable pageable
    );

    Optional<Organization> findByIdAndIsDeletedFalse(Long id);

    boolean existsByCodeAndTypeAndIsDeletedFalse(String code, OrganizationType type);
}
