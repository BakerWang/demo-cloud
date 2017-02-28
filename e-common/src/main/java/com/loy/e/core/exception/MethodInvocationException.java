package com.loy.e.core.exception;

public class MethodInvocationException extends Throwable {

    private static final long serialVersionUID = -2821702720045751090L;

    public Throwable exception;

    public MethodInvocationException(Throwable exception) {
        this.exception = exception;
    }
}
