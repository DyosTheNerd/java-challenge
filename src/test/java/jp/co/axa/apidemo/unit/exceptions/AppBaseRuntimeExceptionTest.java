package jp.co.axa.apidemo.unit.exceptions;

import jp.co.axa.apidemo.exceptions.AppBaseRuntimeException;
import jp.co.axa.apidemo.exceptions.LockingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AppBaseRuntimeExceptionTest {



    @Test
    public void testMessageGeneration(){
        AppBaseRuntimeException ex = new LockingException(1l,"RESTEST");
        String exceptionMessage = ex.getLocalizedMessage();
        assertTrue("ID passed", exceptionMessage.contains("1"));
        assertTrue("Resource passed", exceptionMessage.contains("RESTEST"));
    }
}


