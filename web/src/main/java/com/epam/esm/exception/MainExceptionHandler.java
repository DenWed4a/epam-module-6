package com.epam.esm.exception;

import com.epam.esm.localization.LocalResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class MainExceptionHandler {
    @Autowired
    LocalResourceManager localResourceManager;
    private final static String SOURCE_NOT_FOUND = "local.exception.source.not.found";
    private final static String NOT_EMPTY = "local.name.not.null.not.empty";

    @ExceptionHandler({ServiceSourceNotFoundException.class})
    public ResponseEntity<ControllerError> handleServiceException(ServiceSourceNotFoundException e) {
        String sourceId = String.format(" (id = %d)", e.getSourceId());
        StringBuilder errorMessage = new StringBuilder(localResourceManager.getMessage(SOURCE_NOT_FOUND));

        errorMessage.append(sourceId);

        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        ControllerError exception = new ControllerError(errorMessage.toString(), 404);

        return new ResponseEntity<>(exception, badRequest);
    }

    @ExceptionHandler({ServiceBlankFieldException.class})
    public ResponseEntity<ControllerError> handleServiceBlankFieldException(ServiceBlankFieldException e) {


        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ControllerError exception = new ControllerError(e.getMessage()+" "+localResourceManager.getMessage(NOT_EMPTY), 404);

        return new ResponseEntity<>(exception, badRequest);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomNotValidArgumentException.class)
    public ValidationErrorResponse handleCustomNotValidException(CustomNotValidArgumentException e) {
        final List<Violation> violations = e.getBindingResult().getAllErrors().stream().map(
                error -> new Violation(localResourceManager.getMessage(error.getDefaultMessage()), 400)
        ).collect(Collectors.toList());


        return new ValidationErrorResponse(violations);
    }



    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(violation -> new Violation(violation.getPropertyPath().toString()+" "+
                        localResourceManager.getMessage(violation.getMessage()), 400))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField()+" "+localResourceManager.getMessage(error.getDefaultMessage()), 400))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ValidationErrorResponse handleAuthenticationException(AuthenticationException e) {
        final List<Violation> violations = List.of( new Violation(e.getMessage(), HttpStatus.UNAUTHORIZED.value()));
        return new ValidationErrorResponse(violations);
    }



    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ValidationErrorResponse handleAuthenticationException(AccessDeniedException e) {
        final List<Violation> violations = List.of( new Violation(e.getMessage(), HttpStatus.UNAUTHORIZED.value()));
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(InvalidUsernameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse handleAuthenticationException(InvalidUsernameException e) {
        final List<Violation> violations = List.of( new Violation(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        return new ValidationErrorResponse(violations);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(JwtAuthenticationException.class)
    public ValidationErrorResponse handleAuthenticationException(JwtAuthenticationException e) {
        final List<Violation> violations = List.of( new Violation(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        return new ValidationErrorResponse(violations);
    }
}
