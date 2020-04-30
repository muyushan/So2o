package com.sane.o2o.web.frontend;

import com.sane.o2o.dto.ShopExecution;
import com.sane.o2o.entity.Area;
import com.sane.o2o.entity.ProductCategory;
import com.sane.o2o.entity.Shop;
import com.sane.o2o.entity.ShopCategory;
import com.sane.o2o.service.AreaService;
import com.sane.o2o.service.ShopCategoryService;
import com.sane.o2o.service.ShopService;
import com.sane.o2o.util.HttpServletRequstUtil;
import com.sane.o2o.util.PathUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ShopListController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;
    @RequestMapping(value = "listshopspageinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShopsPageInfo(HttpServletRequest request){
        Map<String,Object>modalMap=new HashMap<>();
        List<ShopCategory> shopCategoryList = null;
        long parentId= HttpServletRequstUtil.getLong(request,"parentId");
        if(parentId!=-1){
            try{
                ShopCategory shopCategoryParent=new ShopCategory();
                shopCategoryParent.setShopCategoryId(parentId);
                ShopCategory shopCategory=new ShopCategory();
                shopCategory.setParent(shopCategoryParent);
                shopCategoryList=shopCategoryService.getShopCategoryList(shopCategory);
            }catch (Exception ex){
                modalMap.put("success",false);
                modalMap.put("errMsg",ex.getMessage());
            }

        }else{
            try{
                shopCategoryList=shopCategoryService.getShopCategoryList(null);
            }catch (Exception ex){
                modalMap.put("success",false);
                modalMap.put("errMsg",ex.getMessage());
            }

        }
        modalMap.put("shopCategoryList",shopCategoryList);

        List<Area> areaList=null;
        try{
            areaList=areaService.queryAreaList();
            modalMap.put("areaList",areaList);
            modalMap.put("success",true);
            return  modalMap;
        }catch (Exception ex){
            modalMap.put("success",false);
            modalMap.put("errMsg",ex.getMessage());

        }

         return  modalMap;
    }

    @ResponseBody
    @RequestMapping(value = "listshops",method = RequestMethod.GET)
    private Map<String,Object> listShops(HttpServletRequest request){
        Map<String,Object> modalMap=new HashMap<>();
        int pageIndex=HttpServletRequstUtil.getInt(request,"pageIndex");
        int pageSize=HttpServletRequstUtil.getInt(request,"pageSize");
        if(pageIndex>-1 && pageSize>-1){
            long parentId=HttpServletRequstUtil.getLong(request,"parentId");
            long shopCategoryId=HttpServletRequstUtil.getLong(request,"shopCategoryId");
            int areaId=HttpServletRequstUtil.getInt(request,"areaId");
            String shopName=HttpServletRequstUtil.getString(request,"shopName");
            Shop shopCondition=compactShopCondiction4Search(parentId,shopCategoryId,areaId,shopName);
            ShopExecution shopExecution=shopService.queryShopList(shopCondition,pageIndex,pageSize);
            modalMap.put("shopList",shopExecution.getShopList());
            modalMap.put("count",shopExecution.getCount());
            modalMap.put("success",true);
        }else{
            modalMap.put("success",false);
            modalMap.put("errMsg","empty pageSize or pageIndex");
        }
        return modalMap;


    }
    @RequestMapping("/images/**/{imageName}.{suffix}")
    public void getImge(@PathVariable(value = "imageName") String imageName, @PathVariable(value ="suffix") String suffix, HttpServletResponse response, HttpServletRequest request) throws IOException {
        long shopId=HttpServletRequstUtil.getLong(request,"shopId");
        String imagePath= PathUtil.getShopImagePath(shopId);
        String path=PathUtil.getImageStoreBasePath()+imagePath+imageName+"."+suffix;
        response.setDateHeader("Expires",0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("fileName",imageName+suffix);
        response.setHeader("Pragma", "no-cache");
        File imgFile = new File(path);
        BufferedImage bufferedImage= ImageIO.read(imgFile);
        ImageIO.write(bufferedImage,suffix,response.getOutputStream());
    }
    @RequestMapping("/shoplistsearch")
    private String showShopListPage(){
        return "/frontend/shopList";
    }
    private Shop compactShopCondiction4Search(long parentId,long shopCategoryId,int areaId,String shopName){
        Shop shopCondition=new Shop();
        if(parentId!=-1L){
            ShopCategory childCategory=new ShopCategory();
            ShopCategory parentCategory=new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if(shopCategoryId!=-1){
            ShopCategory shopCategory=new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if(areaId!=-1){
            Area area=new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if(StringUtils.isNotEmpty(shopName)){
            shopCondition.setShopName(shopName);
        }
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
