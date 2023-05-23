package com.llqlv.restapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class PersonDto {

    @NotEmpty(message = "Name shouldn't be empty")
    @Size(min = 2, max = 124, message = "name should be between 2 and 124 symbols")
    private String name;

    @Min(value = 0, message = "Age should be greater than 0")
    private int age;

    @NotEmpty(message = "Email shouldn't be empty")
    private String email;

}
