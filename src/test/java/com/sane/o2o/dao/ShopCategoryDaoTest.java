package com.sane.o2o.dao;

import com.sane.o2o.BaseTest;
import com.sane.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ShopCategoryDaoTest extends BaseTest {
@Autowired
    private ShopCategoryDao shopCategoryDao;
    @Test
    public void queryShopCategory() {
        ShopCategory shopCategory=new ShopCategory();
//        ShopCategory pa=new ShopCategory();
//        pa.setShopCategoryId(10L);
//        shopCategory.setParent(pa);
        List<ShopCategory> categoryList= shopCategoryDao.queryShopCategory(null);
        System.out.println(categoryList.size());
    }


}