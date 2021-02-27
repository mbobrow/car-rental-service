package com.capgemini.carrental.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.capgemini.carrental.dto.request.TenantRequest;
import com.capgemini.carrental.dto.response.TenantResponse;
import com.capgemini.carrental.mapper.TenantEntityToResponseMapper;
import com.capgemini.carrental.mapper.TenantRequestToEntityMapper;
import com.capgemini.carrental.model.Tenant;
import com.capgemini.carrental.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/tenant") @RestController public class TenantController {

    private final TenantService tenantService;
    private final TenantRequestToEntityMapper tenantEntityMapper;
    private final TenantEntityToResponseMapper tenantResponseMapper;

    @Autowired public TenantController(
            final TenantService tenantService,
            final TenantRequestToEntityMapper tenantEntityMapper,
            final TenantEntityToResponseMapper tenantResponseMapper) {
        this.tenantService = tenantService;
        this.tenantEntityMapper = tenantEntityMapper;
        this.tenantResponseMapper = tenantResponseMapper;
    }

    @GetMapping @ResponseBody public List<TenantResponse> getAllTenants() {
        return tenantService.getAllTenants().stream().map(tenantResponseMapper::mapToResponse).collect(Collectors.toList());
    }

    @PostMapping @ResponseBody @ResponseStatus(HttpStatus.CREATED)
    public TenantResponse registerNewTenant(@Valid @RequestBody final TenantRequest tenantRequest) {
        final Tenant tenant = tenantEntityMapper.mapToEntity(tenantRequest);
        final Tenant newTenant = tenantService.addTenant(tenant);
        return tenantResponseMapper.mapToResponse(newTenant);
    }

    @GetMapping(path = "{id}") @ResponseBody public TenantResponse getTenant(@PathVariable("id") final Long id) {
        final Tenant tenant = tenantService.getTenant(id);
        return tenantResponseMapper.mapToResponse(tenant);
    }

    @DeleteMapping(path = "{id}") @ResponseBody public TenantResponse removeTenant(@PathVariable("id") final Long id) {
        final Tenant removedTenant = tenantService.removeTenant(id);
        return tenantResponseMapper.mapToResponse(removedTenant);
    }

    @PutMapping(path = "{id}") @ResponseBody public TenantResponse updateTenant(
            @PathVariable("id") final Long id, @Valid @RequestBody final TenantRequest updatedTenantRequest) {
        final Tenant tenantToUpdate = tenantEntityMapper.mapToEntity(updatedTenantRequest);
        final Tenant updatedTenant = tenantService.updateTenant(id, tenantToUpdate);
        return tenantResponseMapper.mapToResponse(updatedTenant);
    }
}

