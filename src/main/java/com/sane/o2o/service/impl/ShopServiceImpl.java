package com.sane.o2o.service.impl;

import com.sane.o2o.dao.ShopDao;
import com.sane.o2o.dto.ShopExecution;
import com.sane.o2o.entity.Shop;
import com.sane.o2o.enums.ShopStateEnum;
import com.sane.o2o.exceptions.ShopOperationException;
import com.sane.o2o.service.ShopService;
import com.sane.o2o.util.ImgUtil;
import com.sane.o2o.util.PageUtil;
import com.sane.o2o.util.PathUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop,MultipartFile shopImg) throws ShopOperationException{
        if(shop==null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            if(StringUtils.isEmpty(shop.getShopName())){
                throw new ShopOperationException("店铺名称不能为空");
            }
            if(StringUtils.isEmpty(shop.getPhone())){
                throw new ShopOperationException("联系电话不能为空");
            }
            if(StringUtils.isEmpty(shop.getShopDesc())){
                throw new ShopOperationException("店铺描述不能为空");
            }
            if(shop.getArea()==null||shop.getArea().getAreaId()==null){
                throw new ShopOperationException("请选择店铺所属区域");
            }
            if(shop.getShopCategory()==null||shop.getShopCategory().getShopCategoryId()==null){
                throw new ShopOperationException("请选择店铺所属类别");
            }
            if(StringUtils.isEmpty(shop.getShopAddr())){
                throw new ShopOperationException("店铺详细地址不能为空");
            }
            if(shop.getPersonInfo()==null||shop.getPersonInfo().getUserId()==null){
                throw new ShopOperationException("没有获取到登录信息，请重新登录后再操作");
            }
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            int effectRow=shopDao.insertShop(shop);
            if(effectRow<=0){
                throw new ShopOperationException("增加店铺失败");
            }else{
                if(shopImg!=null){
                    try{
                        addShopImg(shop,shopImg);
                    }
                    catch (Exception ex){
                        throw  new ShopOperationException("保存店铺图片失败:"+ex.getMessage());
                    }
                     effectRow=shopDao.updateShop(shop);
                    if(effectRow<=0){
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        }catch (Exception ex){
            throw new ShopOperationException("增加店铺异常："+ex.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    @Override
    public Shop queryShopById(Long shopId) throws ShopOperationException {
        Shop shop=shopDao.queryByShopId(shopId);
        return shop;
    }

    @Override
    public ShopExecution modifyShop(Shop shop, MultipartFile shopImg) throws ShopOperationException{
        if(shop==null|| shop.getShopId()==null){
            return   new ShopExecution(ShopStateEnum.NULL_SHOP);
        }else{
            try{
                if(shopImg!=null){
                    Shop tempShop=queryShopById(shop.getShopId());
                    if(tempShop!=null&& StringUtils.isNotEmpty(tempShop.getShopImg())){
                        ImgUtil.deleteFileOrPath(tempShop.getShopImg());
                    }

                    addShopImg(shop,shopImg);
                }
                shop.setLastEditTime(new Date());
                int effectRow=shopDao.updateShop(shop);
                if(effectRow<=0){
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                }else{
                    shop=queryShopById(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS,shop);
                }
            }catch (Exception ex){
                throw  new ShopOperationException("modifyShopError:"+ex.getMessage());
            }

        }
    }

    @Override
    public ShopExecution queryShopList(Shop shop, int pageIndex, int pageSize) {
       int totalCount= shopDao.queryShopCount(shop);
        List<Shop>shopList=shopDao.queryShopList(shop, PageUtil.calcRowIndex(pageIndex,pageSize),pageSize);
        ShopExecution shopExecution=new ShopExecution();

        if(shopList!=null){
            shopExecution.setCount(totalCount);
            shopExecution.setShopList(shopList);
            shopExecution.setState(ShopStateEnum.SUCCESS.getState());
       }else{
            shopExecution.setState(ShopStateEnum.NULL_SHOP.getState());
            shopExecution.setStateInfo(ShopStateEnum.NULL_SHOP.getStateInfo());

        }
        return shopExecution;
    }

    private void addShopImg(Shop shop, MultipartFile shopImg) throws IOException {
       String dest= PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr=ImgUtil.generateThumbnail(shopImg,dest);
        shop.setShopImg(shopImgAddr);
    }
}
