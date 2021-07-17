package com.stc.thamquan.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 08:52
 * Filename  : UserNotFoundAuthenticationException
 */
public class UserNotFoundAuthenticationException extends AuthenticationException {
    public UserNotFoundAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserNotFoundAuthenticationException(String msg) {
        super(msg);
    }
}
