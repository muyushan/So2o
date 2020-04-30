package com.sane.o2o.web.shopadmin;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Producer;
import com.sane.o2o.dto.ShopExecution;
import com.sane.o2o.entity.Area;
import com.sane.o2o.entity.PersonInfo;
import com.sane.o2o.entity.Shop;
import com.sane.o2o.entity.ShopCategory;
import com.sane.o2o.enums.ShopStateEnum;
import com.sane.o2o.service.AreaService;
import com.sane.o2o.service.ShopCategoryService;
import com.sane.o2o.service.ShopService;
import com.sane.o2o.util.CodeUtil;
import com.sane.o2o.util.HttpServletRequstUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/shopadmin")
public class ShopManagmentController {
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private Producer producer;
    @ResponseBody
    @RequestMapping(value = "/registershop",method = RequestMethod.POST)
    public Map<String,Object> registerShop(@RequestParam(value = "shopImg",required=false) MultipartFile commonsMultipartFile, HttpServletRequest request){
        Map<String,Object> registerShop=new HashMap<>();
        if(!CodeUtil.verifyCode(CodeUtil.REGIST_SHOP_VERIFYCODE_KEY,"verifyCode",request)){
            registerShop.put("success",false);
            registerShop.put("errMsg","验证码错误");
            return registerShop;
        }
       String shopStr= HttpServletRequstUtil.getString(request,"shopStr");
       Shop shop=JSON.parseObject(shopStr, Shop.class);
        PersonInfo personInfo =new PersonInfo();
        personInfo.setUserId(8L);
       shop.setPersonInfo(personInfo);
       try {
           ShopExecution shopExecution=shopService.addShop(shop,commonsMultipartFile);
           registerShop.put("success",shopExecution.getState()== ShopStateEnum.CHECK.getState()?true:false);
           if(shopExecution.getState()==ShopStateEnum.CHECK.getState()){
               List<Shop> shopList= (List<Shop>) request.getSession().getAttribute("shopList");
               if(CollectionUtils.isEmpty(shopList)){
                   shopList=new ArrayList<>();
               }
                   shopList.add(shopExecution.getShop());
               request.getSession().setAttribute("shopList",shopList);
           }
       }catch (Exception ex){
           registerShop.put("success",false);
           registerShop.put("errMsg",ex.getMessage());
       }
        return registerShop;
    }

    @ResponseBody
    @RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
    public Map<String,Object> modifyShop(@RequestParam(value = "shopImg",required=false) MultipartFile commonsMultipartFile, HttpServletRequest request){
        Map<String,Object> modifyShop=new HashMap<>();
        if(!CodeUtil.verifyCode(CodeUtil.REGIST_SHOP_VERIFYCODE_KEY,"verifyCode",request)){
            modifyShop.put("success",false);
            modifyShop.put("errMsg","验证码错误");
            return modifyShop;
        }
        String shopStr= HttpServletRequstUtil.getString(request,"shopStr");
        Shop shop=JSON.parseObject(shopStr, Shop.class);
        PersonInfo personInfo =new PersonInfo();
        personInfo.setUserId(8L);
        shop.setPersonInfo(personInfo);
        try {
            if(shop!=null&&shop.getShopId()!=null){
                ShopExecution shopExecution=shopService.modifyShop(shop,commonsMultipartFile);
                modifyShop.put("success",shopExecution.getState()== ShopStateEnum.SUCCESS.getState()?true:false);
            }else{
                modifyShop.put("success",false);
                modifyShop.put("errMsg","店铺ID为空");
            }

        }catch (Exception ex){
            modifyShop.put("success",false);
            modifyShop.put("errMsg",ex.getMessage());
        }
        return modifyShop;
    }
    @RequestMapping("/shopOperation")
    public String shopOperation(){
        return "shop/shopOperation";
    }

    @ResponseBody
    @RequestMapping(value = "getshopmangementnnfo",method = RequestMethod.GET)
    public Map<String,Object>getShopMangementInfo(HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap<>();
        Long shopId=HttpServletRequstUtil.getLong(request,"shopId");
        if(shopId<=0){
            Object currentShopObject=request.getSession().getAttribute("currentShop");
            if(currentShopObject==null){
                resultMap.put("redirect",true);
                resultMap.put("url","/So2o/shopadmin/shopList");
            }else{
                Shop currentShop=(Shop) currentShopObject;
                resultMap.put("redirect",false);
                resultMap.put("shopId",currentShop.getShopId());
            }
        }else{
            Shop currentShop=new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("shop",currentShop);
            resultMap.put("redirect",false);
        }
        return  resultMap;

    }

    @RequestMapping("/shopmanagement")
    public String shopmanagement() {
        return "shop/shopManagment";
    }

    @RequestMapping(value = "/getShopList",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryShopList(HttpServletRequest request){
        PersonInfo personInfo=new PersonInfo();
        personInfo.setUserId(8L);
        personInfo.setName("张三");
        request.getSession().setAttribute("user",personInfo);
        personInfo=(PersonInfo) request.getSession().getAttribute("user");
        Shop shop=new Shop();
        shop.setPersonInfo(personInfo);
        ShopExecution shopExecution=shopService.queryShopList(shop,1,100);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("shopList",shopExecution.getShopList());
        resultMap.put("user",personInfo);
        return  resultMap;
    }

    @RequestMapping("/shopList")
    public String shopListPage(){
        return "shop/shopList";
    }

    @RequestMapping(value = "/getshopbyid" ,method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object>getShopById(HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap<>();
        Long shopId=HttpServletRequstUtil.getLong(request,"shopId");
        if(shopId>0){
            try{
                Shop shop=shopService.queryShopById(shopId);
                List<Area> areaList=areaService.queryAreaList();
                List<ShopCategory> shopCategoryList=shopCategoryService.getShopCategoryList(new ShopCategory());

                resultMap.put("shop",shop);
                resultMap.put("shopCategoryList",shopCategoryList);
                resultMap.put("areaList",areaList);
                resultMap.put("success",true);

            }catch (Exception ex){
                resultMap.put("success",false);
                resultMap.put("message",ex.getMessage());
            }

        }else{
            resultMap.put("success",false);
            resultMap.put("message","empty shopId");
        }
        return  resultMap;

    }

    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getShopInitInfo(){
        Map<String,Object> modelMap=new HashMap<>();
        List<ShopCategory> shopCategoryList=new ArrayList<>();
        List<Area> areaList=new ArrayList<>();
        try{
            shopCategoryList=shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList=areaService.queryAreaList();
            modelMap.put("success",true);
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("areaList",areaList);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errorMsg",e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping("captcha.jpg")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String captchaText = producer.createText();
        request.getSession().setAttribute(CodeUtil.REGIST_SHOP_VERIFYCODE_KEY, captchaText);
        try (ServletOutputStream outputStream = response.getOutputStream();
        ) {
            response.setDateHeader("Expires",0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            BufferedImage bufferedImage = producer.createImage(captchaText);
            response.setContentType("image/jpeg");
            ImageIO.write(bufferedImage, "jpg", outputStream);
        }
    }
}
