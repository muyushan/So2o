package com.sane.o2o.dao;

import com.sane.o2o.entity.Product;
import com.sane.o2o.entity.ProductImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {
    public  int insertProduct(Product product);
    public  int modifyProduct(Product product);
    public  List<Product> quereyProductList(@Param(value ="product") Product product,@Param(value = "rowIndex") int rowIndex,@Param("pageSize") int pageSize);
    public  int quereyProductCount(@Param(value ="product") Product product);
    public int insertProductImage(@Param("productImgs") List<ProductImg> productImgs);
    public  int updateProductCategoryToNull(@Param("categoryId") Long productCategoryId);
    public List<ProductImg> queryProductImageByProductId(Long productid);
    public  int deleteProductImgByProductId(Long productId);
    public Product queryProductById(Long productId);
}
