package com.sane.o2o.web.frontend;

import com.sane.o2o.entity.HeadLine;
import com.sane.o2o.entity.Shop;
import com.sane.o2o.entity.ShopCategory;
import com.sane.o2o.service.HeadLineService;
import com.sane.o2o.service.ShopCategoryService;
import com.sane.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class MainPageController {
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private HeadLineService headLineService;

    @RequestMapping("/index")
    public  String showIndexPage(){
        return "frontend/index";
    }
    @RequestMapping("listmainpageinfo")
    @ResponseBody
    public Map<String,Object> listMainPageInfo(){
        Map<String,Object> result=new HashMap<>();
        List<ShopCategory> shopCategoryList=new ArrayList<>();
        shopCategoryList= shopCategoryService.getShopCategoryList(null);
        HeadLine headLine=new HeadLine();
        headLine.setEnableStatus(1);
        List<HeadLine> headLineList=headLineService.getHeadLineList(headLine);
        result.put("success",true);
        result.put("headLineList",headLineList);
        result.put("shopCategoryList",shopCategoryList);
        return result;
    }

    @RequestMapping("/{subfolder}/**/{imageName}.{suffix}")
    public void getImge(@PathVariable("subfolder") String subfolder,@PathVariable(value = "imageName") String imageName, @PathVariable(value ="suffix") String suffix, HttpServletResponse response, HttpServletRequest request) throws IOException {
        Shop currentShop= (Shop) request.getSession().getAttribute("shop");
        String imagePath= "/upload/images/item/"+subfolder+"/";
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

}
