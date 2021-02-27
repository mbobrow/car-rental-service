package com.capgemini.carrental.mapper;

import java.util.stream.Collectors;

import com.capgemini.carrental.dto.response.RentalResponse;
import com.capgemini.carrental.model.Rental;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service public class RentalEntityToResponseMapper implements EntityResponseMapper<Rental, RentalResponse> {

    private final ModelMapper modelMapper;
    private final TenantEntityToResponseMapper tenantResponseMapper;
    private final CarEntityToResponseMapper carResponseMapper;

    public RentalEntityToResponseMapper(
            final ModelMapper modelMapper,
            final TenantEntityToResponseMapper tenantResponseMapper,
            final CarEntityToResponseMapper carResponseMapper) {
        this.modelMapper = modelMapper;
        this.tenantResponseMapper = tenantResponseMapper;
        this.carResponseMapper = carResponseMapper;
    }

    @Override public RentalResponse mapToResponse(final Rental entity) {
        final RentalResponse rentalResponse = modelMapper.map(entity, RentalResponse.class);
        rentalResponse.setTenant(tenantResponseMapper.mapToResponse(entity.getTenant()));
        rentalResponse.setRentedCars(entity.getRentedCars()
                .stream()
                .map(carResponseMapper::mapToResponse)
                .collect(Collectors.toList()));
        return rentalResponse;
    }
}
