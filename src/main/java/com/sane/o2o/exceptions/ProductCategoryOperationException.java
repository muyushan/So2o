package com.sane.o2o.exceptions;

public class ProductCategoryOperationException extends RuntimeException {

    public ProductCategoryOperationException(String errorMsg){
        super(errorMsg);
    }
}
