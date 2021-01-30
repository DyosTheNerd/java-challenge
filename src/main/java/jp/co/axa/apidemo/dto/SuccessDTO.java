package jp.co.axa.apidemo.dto;

import lombok.Getter;
import lombok.Setter;

public class SuccessDTO {

    @Getter
    @Setter
    boolean successful;

    public SuccessDTO(boolean success){
        successful = success;
    }
}
