package com.vhl.htqt.organization.service;

import com.vhl.htqt.common.exception.BusinessException;
import com.vhl.htqt.common.exception.ResourceNotFoundException;
import com.vhl.htqt.organization.dto.OrganizationRequest;
import com.vhl.htqt.organization.dto.OrganizationResponse;
import com.vhl.htqt.organization.entity.Organization;
import com.vhl.htqt.organization.entity.OrganizationType;
import com.vhl.htqt.organization.repository.OrganizationRepository;
import com.vhl.htqt.organization.repository.OrganizationMemberRepository;
import com.vhl.htqt.common.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    private final OrganizationMemberRepository organizationMemberRepository;

    public PageResponse<OrganizationResponse> getOrganizationsByType(
            OrganizationType type,
            String keyword,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Organization> organizationPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            organizationPage = organizationRepository.findByTypeAndIsDeletedFalseOrderByIdDesc(
                    type,
                    pageable
            );
        } else {
            String searchKeyword = keyword.trim();

            organizationPage =
                    organizationRepository.findByTypeAndIsDeletedFalseAndCodeContainingIgnoreCaseOrTypeAndIsDeletedFalseAndNameContainingIgnoreCaseOrderByIdDesc(
                            type,
                            searchKeyword,
                            type,
                            searchKeyword,
                            pageable
                    );
        }

        List<OrganizationResponse> content = organizationPage
                .getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return PageResponse.<OrganizationResponse>builder()
                .content(content)
                .page(organizationPage.getNumber())
                .size(organizationPage.getSize())
                .totalElements(organizationPage.getTotalElements())
                .totalPages(organizationPage.getTotalPages())
                .last(organizationPage.isLast())
                .build();
    }

    public OrganizationResponse getOrganizationById(Long id) {
        Organization organization = findOrganizationById(id);
        return toResponse(organization);
    }

    public OrganizationResponse createOrganization(OrganizationType type, OrganizationRequest request) {
        boolean existsCode = organizationRepository.existsByCodeAndTypeAndIsDeletedFalse(
                request.getCode(),
                type
        );

        if (existsCode) {
            throw new BusinessException("Mã tổ chức đã tồn tại");
        }

        Organization organization = new Organization();
        organization.setCode(request.getCode());
        organization.setName(request.getName());
        organization.setType(type);
        organization.setRegion(request.getRegion());
        organization.setCountry(request.getCountry());
        organization.setAddress(request.getAddress());
        organization.setEmail(request.getEmail());
        organization.setPhone(request.getPhone());
        organization.setWebsite(request.getWebsite());
        organization.setContactPerson(request.getContactPerson());
        organization.setDescription(request.getDescription());

        if (request.getIsActive() != null) {
            organization.setIsActive(request.getIsActive());
        }

        Organization parent = getAndValidateParentForCreate(type, request.getParentId());
        organization.setParent(parent);

        Organization savedOrganization = organizationRepository.save(organization);
        return toResponse(savedOrganization);
    }

    public OrganizationResponse updateOrganization(Long id, OrganizationRequest request) {
        Organization organization = findOrganizationById(id);

        boolean isCodeChanged = !organization.getCode().equals(request.getCode());

        if (isCodeChanged) {
            boolean existsCode = organizationRepository.existsByCodeAndTypeAndIsDeletedFalse(
                    request.getCode(),
                    organization.getType()
            );

            if (existsCode) {
                throw new BusinessException("Mã tổ chức đã tồn tại");
            }
        }

        organization.setCode(request.getCode());
        organization.setName(request.getName());
        organization.setRegion(request.getRegion());
        organization.setCountry(request.getCountry());
        organization.setAddress(request.getAddress());
        organization.setEmail(request.getEmail());
        organization.setPhone(request.getPhone());
        organization.setWebsite(request.getWebsite());
        organization.setContactPerson(request.getContactPerson());
        organization.setDescription(request.getDescription());

        if (request.getIsActive() != null) {
            organization.setIsActive(request.getIsActive());
        }

        Organization parent = getAndValidateParentForUpdate(organization, request.getParentId());
        organization.setParent(parent);

        Organization savedOrganization = organizationRepository.save(organization);
        return toResponse(savedOrganization);
    }

    public void deleteOrganization(Long id) {
        Organization organization = findOrganizationById(id);
        validateCanDeleteOrganization(organization);
        organization.setIsDeleted(true);
        organizationRepository.save(organization);
    }

    private Organization findOrganizationById(Long id) {
        return organizationRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tổ chức"));
    }

    private OrganizationResponse toResponse(Organization organization) {
        Long parentId = null;
        String parentName = null;

        if (organization.getParent() != null) {
            parentId = organization.getParent().getId();
            parentName = organization.getParent().getName();
        }

        return OrganizationResponse.builder()
                .id(organization.getId())
                .code(organization.getCode())
                .name(organization.getName())
                .type(organization.getType())
                .parentId(parentId)
                .parentName(parentName)
                .region(organization.getRegion())
                .country(organization.getCountry())
                .address(organization.getAddress())
                .email(organization.getEmail())
                .phone(organization.getPhone())
                .website(organization.getWebsite())
                .contactPerson(organization.getContactPerson())
                .description(organization.getDescription())
                .isActive(organization.getIsActive())
                .createdAt(organization.getCreatedAt())
                .updatedAt(organization.getUpdatedAt())
                .build();
    }

    private Organization getAndValidateParentForCreate(
            OrganizationType type,
            Long parentId
    ) {
        if (parentId == null) {
            return null;
        }

        if (type == OrganizationType.EXTERNAL) {
            throw new BusinessException("Tổ chức đối tác ngoài VHL không được có tổ chức cha");
        }

        Organization parent = findOrganizationById(parentId);

        if (parent.getType() != OrganizationType.INTERNAL) {
            throw new BusinessException("Tổ chức cha phải là tổ chức trong VHL");
        }

        return parent;
    }

    private Organization getAndValidateParentForUpdate(
            Organization organization,
            Long parentId
    ) {
        if (parentId == null) {
            return null;
        }

        if (organization.getType() == OrganizationType.EXTERNAL) {
            throw new BusinessException("Tổ chức đối tác ngoài VHL không được có tổ chức cha");
        }

        if (organization.getId().equals(parentId)) {
            throw new BusinessException("Tổ chức không được chọn chính nó làm tổ chức cha");
        }

        Organization parent = findOrganizationById(parentId);

        if (parent.getType() != OrganizationType.INTERNAL) {
            throw new BusinessException("Tổ chức cha phải là tổ chức trong VHL");
        }

        return parent;
    }

    private void validateCanDeleteOrganization(Organization organization) {
        boolean hasChildren = organizationRepository.existsByParentIdAndIsDeletedFalse(
                organization.getId()
        );

        if (hasChildren) {
            throw new BusinessException("Không thể xóa tổ chức vì vẫn còn tổ chức con");
        }

        if (organization.getType() == OrganizationType.INTERNAL) {
            boolean hasMembers = organizationMemberRepository.existsByOrganizationIdAndIsDeletedFalse(
                    organization.getId()
            );

            if (hasMembers) {
                throw new BusinessException("Không thể xóa tổ chức vì vẫn còn thành viên trong tổ chức");
            }
        }
    }
}
