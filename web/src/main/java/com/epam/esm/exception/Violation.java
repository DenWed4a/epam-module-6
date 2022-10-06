package com.epam.esm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Violation {


    private final String message;
    private final int errorCode;


}