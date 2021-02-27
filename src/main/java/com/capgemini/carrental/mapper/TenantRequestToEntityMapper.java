package com.capgemini.carrental.mapper;

import com.capgemini.carrental.dto.request.TenantRequest;
import com.capgemini.carrental.model.Tenant;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service public class TenantRequestToEntityMapper implements RequestEntityMapper<TenantRequest, Tenant> {

    private final ModelMapper modelMapper;

    public TenantRequestToEntityMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override public Tenant mapToEntity(final TenantRequest request) {
        final Tenant tenant = modelMapper.map(request, Tenant.class);
        tenant.setGender(request.getGenderConverted());
        return tenant;
    }
}
