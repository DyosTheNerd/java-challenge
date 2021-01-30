package jp.co.axa.apidemo.exceptions;


import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class ResourceNotFoundException extends AppBaseRuntimeException{




    public ResourceNotFoundException(long id, String resourceType) {
        super(id, resourceType);
        messageKey = "exceptions.resource.not.found";
    }




}
