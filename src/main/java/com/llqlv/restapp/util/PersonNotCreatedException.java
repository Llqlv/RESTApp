package com.llqlv.restapp.util;

public class PersonNotCreatedException extends RuntimeException{

    public PersonNotCreatedException(String msg) {
        super(msg);
    }
}
