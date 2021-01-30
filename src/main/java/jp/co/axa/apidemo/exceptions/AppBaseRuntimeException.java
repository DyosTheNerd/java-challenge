package jp.co.axa.apidemo.exceptions;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;


public class AppBaseRuntimeException extends RuntimeException {

    @Getter
    protected long id;

    @Getter
    protected String resourceType;





    String messageKey;

    public AppBaseRuntimeException(long id, String resourceType){
        this.id = id;
        this.resourceType = resourceType;

        messageKey = "exceptions.default";
    }


    @Override
    public String getLocalizedMessage(){
        Locale.setDefault(Locale.ENGLISH);
        ResourceBundleMessageSource source = new  ResourceBundleMessageSource();
        source.setBasename("i18n/messages");

        source.setUseCodeAsDefaultMessage(true);

        return  source.getMessage(messageKey, new Object[]{resourceType,id},Locale.getDefault());
    }



}
