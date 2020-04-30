package com.sane.o2o.service;

import com.sane.o2o.dto.ProductCategoryExecution;
import com.sane.o2o.entity.ProductCategory;
import com.sane.o2o.exceptions.ProductCategoryOperationException;

import java.util.List;

public interface ProductCategoryService {
    public List<ProductCategory> getProductCategoryList(Long shopId);
    public ProductCategoryExecution batchInserProductCategory(List<ProductCategory> productCategoryList)throws ProductCategoryOperationException;
    public ProductCategoryExecution deleteProductCategory(long categoryId)throws ProductCategoryOperationException;
}
