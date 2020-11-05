package com.capgemini.rentalcars.api;

import com.capgemini.rentalcars.model.Tenant;
import com.capgemini.rentalcars.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("api/v1/tenant")
@RestController
public class TenantController {

    private final TenantService tenantService;

    @Autowired
    public TenantController(final TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public List<Tenant> getAllTenants() {
        return this.tenantService.getAllTenants();
    }

    @PostMapping
    public Tenant registerNewTenant(@Valid @RequestBody final Tenant tenant) {
        return this.tenantService.addTenant(tenant);
    }

    @GetMapping(path = "{id}")
    public Tenant getTenant(@PathVariable("id") final Long id) {
        return this.tenantService.getTenant(id);
    }

    @DeleteMapping(path = "{id}")
    public Tenant removeTenant(@PathVariable("id") final Long id) {
        return this.tenantService.removeTenant(id);
    }

    @PutMapping(path = "{id}")
    public Tenant updateTenant(@PathVariable("id") final Long id, @Valid @RequestBody final Tenant updatedTenant) {
        return this.tenantService.updateTenant(id, updatedTenant);
    }
}

