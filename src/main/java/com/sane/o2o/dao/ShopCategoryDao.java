package com.sane.o2o.dao;

import com.sane.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopCategoryDao {
    List<ShopCategory>queryShopCategory(@Param("category")ShopCategory category);
}

