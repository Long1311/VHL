package com.vhl.htqt.organization.controller;

import com.vhl.htqt.organization.dto.OrganizationRequest;
import com.vhl.htqt.organization.dto.OrganizationResponse;
import com.vhl.htqt.organization.entity.OrganizationType;
import com.vhl.htqt.organization.service.OrganizationService;
import com.vhl.htqt.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping("/internal")
    public PageResponse<OrganizationResponse> getInternalOrganizations(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return organizationService.getOrganizationsByType(
                OrganizationType.INTERNAL,
                keyword,
                page,
                size
        );
    }

    @PostMapping("/internal")
    public OrganizationResponse createInternalOrganization(
            @Valid @RequestBody OrganizationRequest request
    ) {
        return organizationService.createOrganization(OrganizationType.INTERNAL, request);
    }

    @GetMapping("/external")
    public PageResponse<OrganizationResponse> getExternalOrganizations(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return organizationService.getOrganizationsByType(
                OrganizationType.EXTERNAL,
                keyword,
                page,
                size
        );
    }

    @PostMapping("/external")
    public OrganizationResponse createExternalOrganization(
            @Valid @RequestBody OrganizationRequest request
    ) {
        return organizationService.createOrganization(OrganizationType.EXTERNAL, request);
    }

    @GetMapping("/{id}")
    public OrganizationResponse getOrganizationById(@PathVariable Long id) {
        return organizationService.getOrganizationById(id);
    }

    @PutMapping("/{id}")
    public OrganizationResponse updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationRequest request
    ) {
        return organizationService.updateOrganization(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
    }
}
