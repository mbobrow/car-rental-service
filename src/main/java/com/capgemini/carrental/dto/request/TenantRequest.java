package com.capgemini.carrental.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import com.capgemini.carrental.model.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data public class TenantRequest {

    @NotBlank
    private String name;
    private String gender;
    @Positive
    private Integer age;

    @JsonIgnore public Gender getGenderConverted() {
        return Gender.getIfPresent(gender);
    }

}
