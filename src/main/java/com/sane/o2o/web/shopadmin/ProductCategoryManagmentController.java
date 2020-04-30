package com.sane.o2o.web.shopadmin;

import com.sane.o2o.dao.ProductDao;
import com.sane.o2o.dto.ProductCategoryExecution;
import com.sane.o2o.dto.Result;
import com.sane.o2o.entity.Product;
import com.sane.o2o.entity.ProductCategory;
import com.sane.o2o.entity.Shop;
import com.sane.o2o.enums.ProductCategoryStateEnum;
import com.sane.o2o.exceptions.ProductCategoryOperationException;
import com.sane.o2o.service.ProductCategoryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagmentController {
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductDao productDao;

    @ResponseBody
    @RequestMapping("getproductcategorylist")
    public Result<ProductCategory> getProductCategoryList(HttpServletRequest request){
        Shop currentShop= (Shop) request.getSession().getAttribute("shop");
        List<ProductCategory> productCategoryList=productCategoryService.getProductCategoryList(currentShop.getShopId());
        Result<ProductCategory> productCategoryResult=new Result(true,productCategoryList);
        return  productCategoryResult;
    }
    @RequestMapping("/productcategorymanage")
    public String productCategoryPage(){
        return "shop/productCategoryManagment";
    }
    @ResponseBody
    @RequestMapping(value = "/addproductcategory",method = RequestMethod.POST)
    public Map<String,Object> addProductCategories(@RequestBody List<ProductCategory> productCategoryList,HttpServletRequest request){
        Shop currentShop=(Shop) request.getSession().getAttribute("shop");
        Map<String,Object> resultMap=new HashMap<>();
        if(currentShop==null){
            resultMap.put("success",false);
            resultMap.put("errMsg","没有获取到店铺信息，请重新选择店铺");
            return resultMap;
        }
        if(CollectionUtils.isEmpty(productCategoryList)){
            resultMap.put("success",false);
            resultMap.put("errMsg","请输入商品类别");
            return  resultMap;
        }
       for(ProductCategory productCategory:productCategoryList){
           productCategory.setShopId(currentShop.getShopId());
       }

        try {
            ProductCategoryExecution productCategoryExecution=productCategoryService.batchInserProductCategory(productCategoryList);
          if(productCategoryExecution.getState()==ProductCategoryStateEnum.SUCCESS.getState()){
              resultMap.put("success",true);
          }else{
              resultMap.put("success",false);
              resultMap.put("errMsg",productCategoryExecution.getStateInfo());
          }
        }catch (ProductCategoryOperationException ex){
       }
        return resultMap;
    }
    @RequestMapping("/deletecategory")
    @ResponseBody
    public Map<String,Object> deleteProductCategory(Long categoryId){
        productDao.updateProductCategoryToNull(categoryId);
        ProductCategoryExecution productCategoryExecution=productCategoryService.deleteProductCategory(categoryId);
        Map<String,Object> result=new HashMap<>();
        result.put("success",productCategoryExecution.getState()==ProductCategoryStateEnum.SUCCESS.getState()?true:false);
        return result;

    }
}
