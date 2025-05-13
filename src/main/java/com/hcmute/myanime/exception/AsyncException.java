package com.hcmute.myanime.exception;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class AsyncException implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... args) {
        System.out.println("Method Name" + method.getName()
                + "---" + Arrays.toString(args) + "----"
                + "error Message: " + ex.getMessage());

    }
}
