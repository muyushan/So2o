package com.sane.o2o.dao;

import com.sane.o2o.BaseTest;
import com.sane.o2o.entity.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductCategoryDaoTest extends BaseTest {
    @Autowired
    private  ProductCategoryDao productCategoryDao;
    @Test
    public  void testQueryProductCategory(){
        List<ProductCategory> productCategoryList= productCategoryDao.queryProductCategoryList(20L);
        Assert.assertEquals(5,productCategoryList.size());
    }

    @Test
    public void testProductCategoryBatchInsert(){
        List<ProductCategory> productCategoryList=new ArrayList<>();
        for(int i=0;i<10;i++){
            ProductCategory productCategory=new ProductCategory();
            productCategory.setCreateTime(new Date());
            productCategory.setPriority(i);
            productCategory.setProductCategoryName("cc"+i);
            productCategory.setShopId(16L);
            productCategoryList.add(productCategory);
        }
        int count=productCategoryDao.batchInsertProductCategory(productCategoryList);
        Assert.assertEquals(10,count);
    }

    @Test
    public void testDelete(){

        int count=productCategoryDao.deleteProductCategoryById(36L);
        Assert.assertEquals(1,count);
    }
}
