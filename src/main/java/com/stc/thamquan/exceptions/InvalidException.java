package com.stc.thamquan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 08:51
 * Filename  : InvalidException
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidException extends RuntimeException {
    public InvalidException() {
    }

    public InvalidException(String message) {
        super(message);
    }

    public InvalidException(List<String> error) {
        super(String.valueOf(error));
    }
}

