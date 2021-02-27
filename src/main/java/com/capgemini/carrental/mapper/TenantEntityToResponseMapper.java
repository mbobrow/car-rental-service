package com.capgemini.carrental.mapper;

import com.capgemini.carrental.dto.response.TenantResponse;
import com.capgemini.carrental.model.Tenant;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service public class TenantEntityToResponseMapper implements EntityResponseMapper<Tenant, TenantResponse> {

    private final ModelMapper modelMapper;

    public TenantEntityToResponseMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override public TenantResponse mapToResponse(final Tenant entity) {
        return modelMapper.map(entity, TenantResponse.class);
    }
}
