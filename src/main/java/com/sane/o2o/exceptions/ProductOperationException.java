package com.sane.o2o.exceptions;

public class ProductOperationException extends RuntimeException {

    public ProductOperationException(String errorMsg){
        super(errorMsg);
    }
}
