package com.example.demo.advice;

import com.example.demo.exception.InvalidNameException;
import com.example.demo.exception.UserNotFoundException;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@Slf4j
@GrpcAdvice
public class GlobalExceptionHandler {

    @GrpcExceptionHandler(InvalidNameException.class)
    public Status handleInvalidName(InvalidNameException e) {
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(UserNotFoundException.class)
    public Status handleUserNotFound(UserNotFoundException e) {
        return Status.NOT_FOUND.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(Exception.class)
    public Status handleOtherExceptions(Exception e) {
        return Status.UNKNOWN.withDescription("Unexpected error");
    }
}
