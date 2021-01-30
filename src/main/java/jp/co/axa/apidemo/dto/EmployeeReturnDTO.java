package jp.co.axa.apidemo.dto;

import lombok.Getter;
import lombok.Setter;

public class EmployeeReturnDTO extends EmployeeDTO{

    @Getter
    @Setter
    private Long id;


    @Getter
    @Setter
    private Integer version;


}
