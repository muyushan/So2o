package com.sane.o2o.service.impl;

import com.sane.o2o.dao.ProductCategoryDao;
import com.sane.o2o.dto.ProductCategoryExecution;
import com.sane.o2o.entity.ProductCategory;
import com.sane.o2o.enums.ProductCategoryStateEnum;
import com.sane.o2o.exceptions.ProductCategoryOperationException;
import com.sane.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Cacheable(value = "categoryList",key = "#shopId.toString()")
    @Override
    public List<ProductCategory> getProductCategoryList(Long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    @Transactional
    public ProductCategoryExecution batchInserProductCategory(List<ProductCategory> productCategoryList)throws ProductCategoryOperationException {

      if(CollectionUtils.isEmpty(productCategoryList)){
          return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
      }
      try{
          int count= productCategoryDao.batchInsertProductCategory(productCategoryList);
          if(count==productCategoryList.size()){
              return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
          }else{
              throw   new ProductCategoryOperationException("商品类别创建失败");
          }

      }catch (Exception ex){
          throw new ProductCategoryOperationException("batchAddProductCategory"+ex.getMessage());
      }

    }

    @Override
    public ProductCategoryExecution deleteProductCategory(long categoryId) throws ProductCategoryOperationException {

        ProductCategoryExecution productCategoryExecution=null;
        try{
          int count=productCategoryDao.deleteProductCategoryById(categoryId);
          if(count!=1){
              productCategoryExecution=new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
          }else {
              productCategoryExecution=new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
          }
        }catch (Exception ex){
            productCategoryExecution=new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
        }
        return productCategoryExecution;
    }
}