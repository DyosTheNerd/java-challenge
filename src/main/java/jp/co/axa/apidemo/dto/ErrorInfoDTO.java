package jp.co.axa.apidemo.dto;

public class ErrorInfoDTO {

    public final String url;
    public final String exception;

    public ErrorInfoDTO(String url, Exception message) {
        this.url = url;
        this.exception = message.getLocalizedMessage();
    }
}
