package jp.co.axa.apidemo.exceptions;

public class LockingException extends AppBaseRuntimeException {
    public LockingException(long id, String resourceType) {
        super(id, resourceType);
        messageKey = "exceptions.locking";
    }


}
