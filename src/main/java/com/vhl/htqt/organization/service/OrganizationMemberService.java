package com.vhl.htqt.organization.service;

import com.vhl.htqt.common.exception.BusinessException;
import com.vhl.htqt.common.exception.ResourceNotFoundException;
import com.vhl.htqt.organization.dto.OrganizationMemberRequest;
import com.vhl.htqt.organization.dto.OrganizationMemberResponse;
import com.vhl.htqt.organization.entity.Organization;
import com.vhl.htqt.organization.entity.OrganizationMember;
import com.vhl.htqt.organization.entity.OrganizationType;
import com.vhl.htqt.organization.repository.OrganizationMemberRepository;
import com.vhl.htqt.organization.repository.OrganizationRepository;
import com.vhl.htqt.common.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationMemberService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    public PageResponse<OrganizationMemberResponse> getMembersByOrganizationId(
            Long organizationId,
            String keyword,
            int page,
            int size
    ) {
        Organization organization = findOrganizationById(organizationId);
        validateInternalOrganization(organization);

        Pageable pageable = PageRequest.of(page, size);

        Page<OrganizationMember> memberPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            memberPage = organizationMemberRepository
                    .findByOrganizationIdAndIsDeletedFalseOrderByIdDesc(
                            organizationId,
                            pageable
                    );
        } else {
            String searchKeyword = keyword.trim();

            memberPage = organizationMemberRepository
                    .findByOrganizationIdAndIsDeletedFalseAndFullNameContainingIgnoreCaseOrOrganizationIdAndIsDeletedFalseAndEmailContainingIgnoreCaseOrderByIdDesc(
                            organizationId,
                            searchKeyword,
                            organizationId,
                            searchKeyword,
                            pageable
                    );
        }

        List<OrganizationMemberResponse> content = memberPage
                .getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return PageResponse.<OrganizationMemberResponse>builder()
                .content(content)
                .page(memberPage.getNumber())
                .size(memberPage.getSize())
                .totalElements(memberPage.getTotalElements())
                .totalPages(memberPage.getTotalPages())
                .last(memberPage.isLast())
                .build();
    }

    public OrganizationMemberResponse getMemberById(Long organizationId, Long memberId) {
        Organization organization = findOrganizationById(organizationId);
        validateInternalOrganization(organization);

        OrganizationMember member = findMemberByIdAndOrganizationId(memberId, organizationId);
        return toResponse(member);
    }

    public OrganizationMemberResponse createMember(
            Long organizationId,
            OrganizationMemberRequest request
    ) {
        Organization organization = findOrganizationById(organizationId);
        validateInternalOrganization(organization);

        OrganizationMember member = new OrganizationMember();
        member.setOrganization(organization);
        member.setFullName(request.getFullName());
        member.setPosition(request.getPosition());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setDescription(request.getDescription());

        if (request.getIsActive() != null) {
            member.setIsActive(request.getIsActive());
        }

        OrganizationMember savedMember = organizationMemberRepository.save(member);
        return toResponse(savedMember);
    }

    public OrganizationMemberResponse updateMember(
            Long organizationId,
            Long memberId,
            OrganizationMemberRequest request
    ) {
        Organization organization = findOrganizationById(organizationId);
        validateInternalOrganization(organization);

        OrganizationMember member = findMemberByIdAndOrganizationId(memberId, organizationId);

        member.setFullName(request.getFullName());
        member.setPosition(request.getPosition());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setDescription(request.getDescription());

        if (request.getIsActive() != null) {
            member.setIsActive(request.getIsActive());
        }

        OrganizationMember savedMember = organizationMemberRepository.save(member);
        return toResponse(savedMember);
    }

    public void deleteMember(Long organizationId, Long memberId) {
        Organization organization = findOrganizationById(organizationId);
        validateInternalOrganization(organization);

        OrganizationMember member = findMemberByIdAndOrganizationId(memberId, organizationId);

        member.setIsDeleted(true);
        organizationMemberRepository.save(member);
    }

    private Organization findOrganizationById(Long organizationId) {
        return organizationRepository.findByIdAndIsDeletedFalse(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tổ chức"));
    }

    private OrganizationMember findMemberByIdAndOrganizationId(Long memberId, Long organizationId) {
        return organizationMemberRepository
                .findByIdAndOrganizationIdAndIsDeletedFalse(memberId, organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thành viên trong tổ chức"));
    }

    private void validateInternalOrganization(Organization organization) {
        if (organization.getType() != OrganizationType.INTERNAL) {
            throw new BusinessException("Chỉ tổ chức trong VHL mới được quản lý danh sách thành viên");
        }
    }

    private OrganizationMemberResponse toResponse(OrganizationMember member) {
        return OrganizationMemberResponse.builder()
                .id(member.getId())
                .organizationId(member.getOrganization().getId())
                .organizationName(member.getOrganization().getName())
                .fullName(member.getFullName())
                .position(member.getPosition())
                .email(member.getEmail())
                .phone(member.getPhone())
                .description(member.getDescription())
                .isActive(member.getIsActive())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}