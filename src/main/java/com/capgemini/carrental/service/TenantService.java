package com.capgemini.carrental.service;

import java.util.List;
import java.util.Optional;

import com.capgemini.carrental.exception.TenantNotFoundException;
import com.capgemini.carrental.model.Tenant;
import com.capgemini.carrental.repository.TenantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional public class TenantService {

    private final TenantRepository tenantRepository;

    @Autowired public TenantService(final TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    public Tenant addTenant(final Tenant newTenant) {
        return tenantRepository.save(newTenant);
    }

    public Tenant getTenant(final Long id) {
        return tenantRepository.findById(id).orElseThrow(TenantNotFoundException::new);
    }

    public Tenant removeTenant(final Long id) {
        final Optional<Tenant> tenantToRemove = tenantRepository.findById(id);
        if (!tenantToRemove.isPresent()) {
            throw new TenantNotFoundException();
        }
        tenantRepository.deleteById(id);
        return tenantToRemove.get();
    }

    public Tenant updateTenant(final Long id, final Tenant updatedTenant) {
        final Optional<Tenant> tenantToUpdate = tenantRepository.findById(id);
        return tenantToUpdate.map(tenant -> {
            updatedTenant.setId(tenant.getId());
            return tenantRepository.save(updatedTenant);
        }).orElseThrow(TenantNotFoundException::new);
    }
}
