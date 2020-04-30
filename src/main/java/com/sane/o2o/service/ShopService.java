package com.sane.o2o.service;

import com.sane.o2o.dto.ShopExecution;
import com.sane.o2o.entity.Shop;
import com.sane.o2o.exceptions.ShopOperationException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;

public interface ShopService {

    ShopExecution addShop(Shop shop, MultipartFile shopImg) throws ShopOperationException;
    Shop queryShopById(Long shopId);
    ShopExecution modifyShop(Shop shop,MultipartFile shopImg)throws ShopOperationException;
    ShopExecution queryShopList(Shop shop,int pageIndex,int pageSize);

}
