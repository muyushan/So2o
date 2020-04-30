package com.sane.o2o.exceptions;

public class ShopOperationException extends RuntimeException {

    public ShopOperationException(String errorMsg){
        super(errorMsg);
    }
}
