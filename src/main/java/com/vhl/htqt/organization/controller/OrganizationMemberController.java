package com.vhl.htqt.organization.controller;

import com.vhl.htqt.organization.dto.OrganizationMemberRequest;
import com.vhl.htqt.organization.dto.OrganizationMemberResponse;
import com.vhl.htqt.organization.service.OrganizationMemberService;
import com.vhl.htqt.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations/{organizationId}/members")
@RequiredArgsConstructor
public class OrganizationMemberController {

    private final OrganizationMemberService organizationMemberService;

    @GetMapping
    public PageResponse<OrganizationMemberResponse> getMembersByOrganizationId(
            @PathVariable Long organizationId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return organizationMemberService.getMembersByOrganizationId(
                organizationId,
                keyword,
                page,
                size
        );
    }

    @PostMapping
    public OrganizationMemberResponse createMember(
            @PathVariable Long organizationId,
            @Valid @RequestBody OrganizationMemberRequest request
    ) {
        return organizationMemberService.createMember(organizationId, request);
    }

    @GetMapping("/{memberId}")
    public OrganizationMemberResponse getMemberById(
            @PathVariable Long organizationId,
            @PathVariable Long memberId
    ) {
        return organizationMemberService.getMemberById(organizationId, memberId);
    }

    @PutMapping("/{memberId}")
    public OrganizationMemberResponse updateMember(
            @PathVariable Long organizationId,
            @PathVariable Long memberId,
            @Valid @RequestBody OrganizationMemberRequest request
    ) {
        return organizationMemberService.updateMember(organizationId, memberId, request);
    }

    @DeleteMapping("/{memberId}")
    public void deleteMember(
            @PathVariable Long organizationId,
            @PathVariable Long memberId
    ) {
        organizationMemberService.deleteMember(organizationId, memberId);
    }
}
