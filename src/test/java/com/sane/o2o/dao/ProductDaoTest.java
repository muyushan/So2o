package com.sane.o2o.dao;

import com.sane.o2o.BaseTest;
import com.sane.o2o.entity.Product;
import com.sane.o2o.entity.ProductCategory;
import com.sane.o2o.entity.ProductImg;
import com.sane.o2o.entity.Shop;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class ProductDaoTest extends BaseTest {
    @Autowired
    private ProductDao productDao;
    Product product=new Product();
    Long id;
//    @Test
//    public void testAInsertProduct(){
//
//        product.setEnableStatus(1);
//        product.setImgAddr("test");
//        product.setPriority(1);
//        ProductCategory productCategory=new ProductCategory();
//        productCategory.setProductCategoryId(9L);
//        Shop shop=new Shop();
//        shop.setShopId(15L);
//        product.setShop(shop);
//        product.setProductCategory(productCategory);
//        product.setProductDesc("dddd");
//        product.setProductName("cccc");
//        product.setNormalPrice("2");
//        product.setPromotionPrice("5");
//        int count=productDao.insertProduct(product);
//        id=product.getProductId();
//        Assert.assertEquals(1,count);
//    }
    @Test
    public void testBInsertProductImage(){
        List<ProductImg> productImgList=new ArrayList<>();
        for(int i=0;i<10;i++){
            ProductImg productImg=new ProductImg();
            productImg.setImgAddr("ggg");
            productImg.setImgDesc("ccc");
            productImg.setPriority(i);
            productImg.setProductId(16L);
            productImgList.add(productImg);
        }
        int count=productDao.insertProductImage(productImgList);
        Assert.assertEquals(count,10);
    }

    @Test
    public void testCQueryProductImg(){
       List<ProductImg> productImgList= productDao.queryProductImageByProductId(16L);
       Assert.assertEquals(productImgList.size(),10);
    }

    @Test
    public void testDQueryProductById(){
        Product product=productDao.queryProductById(24L);
        Assert.assertNotEquals(null,product);
    }

    @Test
    public void testEDeleteProductImg(){
       int count= productDao.deleteProductImgByProductId(16L);
       Assert.assertEquals(count,10);
    }

}
