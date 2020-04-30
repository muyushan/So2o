package com.sane.o2o.dao;

import com.sane.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {

    int insertShop(Shop shop);

    int updateShop(Shop shop);

    Shop queryByShopId(Long shopId);
    List<Shop> queryShopList(@Param("shopCondiction") Shop shopCondiction,@Param("rowIndex") Integer rowIndex,@Param("pageSize") Integer pageSize);
    Integer  queryShopCount(@Param("shopCondiction") Shop shopCondiction);


}
