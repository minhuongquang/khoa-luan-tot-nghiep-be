package com.stc.thamquan.exceptions;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/31/21
 * Time      : 13:10
 * Filename  : FileStorageException
 */
public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
