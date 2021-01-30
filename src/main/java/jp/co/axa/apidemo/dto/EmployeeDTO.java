package jp.co.axa.apidemo.dto;

import lombok.Getter;
import lombok.Setter;

public class EmployeeDTO {

    @NotNull(message = "First Name cannot be null")
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Integer salary;

    @Getter
    @Setter
    private String department;
}

