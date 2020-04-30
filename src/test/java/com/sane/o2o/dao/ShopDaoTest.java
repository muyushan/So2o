package com.sane.o2o.dao;

import com.sane.o2o.BaseTest;
import com.sane.o2o.dto.ShopExecution;
import com.sane.o2o.entity.Area;
import com.sane.o2o.entity.PersonInfo;
import com.sane.o2o.entity.Shop;
import com.sane.o2o.entity.ShopCategory;
import com.sane.o2o.service.ShopService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ShopDaoTest extends BaseTest {
    @Autowired
   private ShopDao shopDao;
    @Autowired
    private ShopService shopService;
    @Test
//    @Ignore
    public void insertShop() throws Exception {
        Shop shop=new Shop();

        ShopCategory shopCategory=new ShopCategory();
        ShopCategory parentCategory=new ShopCategory();
        parentCategory.setShopCategoryId(27L);
        shopCategory.setParent(parentCategory);
        shop.setShopCategory(shopCategory);
        List<Shop> shopList=shopDao.queryShopList(shop,0,10);
        System.out.println(shopList.size());
    }

    @Test
    @Ignore
    public void editShop() {
        Shop shop=new Shop();

        shop=shopDao.queryByShopId(21L);
        shop.setAdvice("ddfdfd");
      shopService.modifyShop(shop,null);
    }

    @Test
    @Ignore
    public void queryShopByShopId(){
       Shop shop= shopDao.queryByShopId(21l);
       assertEquals("海陆空量贩KTV",shop.getShopName());
    }

    @Test
    public void testQueryShop(){
        Shop shopCondiction=new Shop();
        shopCondiction.setShopName("色");
        ShopExecution shopExecution =shopService.queryShopList(shopCondiction,1,10);
        System.out.println(shopExecution.getShopList().size());

    }
}
