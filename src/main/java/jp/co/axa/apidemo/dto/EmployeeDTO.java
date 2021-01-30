package jp.co.axa.apidemo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class EmployeeDTO {

    @NotBlank
    @Getter
    @Setter
    @Size(min = 1, max = 50)
    private String name;

    @Positive
    @Getter
    @Setter
    private Integer salary;

    @Size(min = 1, max = 20)
    @NotBlank
    @Getter
    @Setter
    private String department;
}

