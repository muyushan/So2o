package com.sane.o2o.web.shopadmin;

import com.alibaba.fastjson.JSON;
import com.sane.o2o.dto.ProductExecution;
import com.sane.o2o.dto.Result;
import com.sane.o2o.entity.Product;
import com.sane.o2o.entity.Shop;
import com.sane.o2o.enums.ProductStateEnum;
import com.sane.o2o.service.ProductService;
import com.sane.o2o.util.ImgUtil;
import com.sane.o2o.util.PathUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("shopadmin")
public class ProductController {
    @Autowired
    private ProductService productService;

    @ResponseBody
    @RequestMapping("/addproduct")
    public Result<Product> addProduct(String productStr, @RequestParam(value = "productImg",required = true) MultipartFile productImg, @RequestParam(value = "imgList",required = false) List<MultipartFile>imgList, HttpServletRequest request){
       Result<Product> result=new Result<>();
        ProductExecution productExecution=null;
        Product product=null;
        if(StringUtils.isEmpty(productStr)){
            result.setErrorMsg("没有获取到商品信息，请填写");
            result.setSuccess(false);
            return result;

        }else{
            product=JSON.parseObject(productStr,Product.class);
        }
        Shop currentShop= (Shop) request.getSession().getAttribute("shop");
        if(currentShop==null){
            result.setErrorMsg("没有获取到当前店铺信息，请重新登录并选择店铺");
            result.setSuccess(false);
            return result;
        }
        product.setShop(currentShop);
        productExecution=productService.addProduct(product,productImg,imgList);
        if(productExecution.getState()==ProductStateEnum.SUCCESS.getState()){
            result.setSuccess(true);
        }else{
            result.setSuccess(false);
            result.setErrorMsg(productExecution.getStateInfo());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("modifyproduct")
    public  ProductExecution modifyProduct(String productStr, @RequestParam(value = "productImg",required = false) MultipartFile productImg, @RequestParam(value = "imgList",required = false) List<MultipartFile>imgList, HttpServletRequest request){

        ProductExecution productExecution=null;
        Product product=null;
        if(StringUtils.isEmpty(productStr)){
            productExecution=new ProductExecution(ProductStateEnum.INNER_ERROR);
            productExecution.setStateInfo("没有获取到商品信息，请填写");
            return productExecution;
        }else{
            product=JSON.parseObject(productStr,Product.class);
        }
        Shop currentShop= (Shop) request.getSession().getAttribute("shop");
        if(currentShop==null){
            productExecution=new ProductExecution(ProductStateEnum.INNER_ERROR);
            productExecution.setStateInfo("没有获取到当前店铺信息，请重新登录并选择店铺");
            return productExecution;
        }
        product.setShop(currentShop);
        productExecution=productService.modifyProduct(product,productImg,imgList);
        return productExecution;
    }

    @RequestMapping("queryproductbyid")
    @ResponseBody
    public Result<Product> quereyProductById(Long productId){
        ProductExecution productExecution=new ProductExecution(ProductStateEnum.SUCCESS);
        productExecution=productService.quereyProductById(productId);
        Result<Product> productResult=new Result<>();

        if(productExecution.getProduct()!=null){
            productResult.setSuccess(true);
            productResult.setData(productExecution.getProduct());
        }else{
            productResult.setSuccess(false);
            productResult.setErrorMsg(productExecution.getStateInfo());
        }
        return productResult;
    }

    @ResponseBody
    @RequestMapping("on_off")
    public Result<String> changeEnableStatus(Long productId){

       ProductExecution productExecution= productService.quereyProductById(productId);
        Result<String> result=new Result<>();

        if(productExecution.getProduct()!=null){
           Product change=new Product();
           change.setProductId(productId);
           change.setEnableStatus(productExecution.getProduct().getEnableStatus()==0?1:0);
           productExecution=productService.changeEnableStatus(change);
           if(productExecution.getState()==ProductStateEnum.SUCCESS.getState()){
               result.setSuccess(true);
           }
       }else {
           result.setSuccess(false);
           result.setErrorMsg("没有查询到对应商品信息");
       }
        return  result;
    }
    @RequestMapping("queryproductlist")
    @ResponseBody
    public ProductExecution quereyProductList(HttpServletRequest request,@RequestParam(required = false) Integer pageIndex,@RequestParam(required = false) Integer pageSize) {
        ProductExecution productExecution=new ProductExecution(ProductStateEnum.SUCCESS);
        Shop currentShop= (Shop) request.getSession().getAttribute("shop");
        if(currentShop==null){
            productExecution=new ProductExecution(ProductStateEnum.INNER_ERROR);
            productExecution.setStateInfo("没有获取到当前店铺信息，请重新登录并选择店铺");
            return productExecution;
        }
        if(pageIndex==null){
            pageIndex=1;
            pageSize=100;
        }
        productExecution=productService.queryProductList(currentShop.getShopId(),pageIndex,pageSize);
        return productExecution;
    }
    @RequestMapping("/images/**/{imageName}.{suffix}")
    public void getImge(@PathVariable(value = "imageName") String imageName, @PathVariable(value ="suffix") String suffix, HttpServletResponse response,HttpServletRequest request) throws IOException {
        Shop currentShop= (Shop) request.getSession().getAttribute("shop");
        String imagePath=PathUtil.getShopImagePath(currentShop.getShopId());
        String path=PathUtil.getImageStoreBasePath()+imagePath+imageName+"."+suffix;
        response.setDateHeader("Expires",0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("fileName",imageName+suffix);
        response.setHeader("Pragma", "no-cache");
        File imgFile = new File(path);
        BufferedImage bufferedImage=ImageIO.read(imgFile);
        ImageIO.write(bufferedImage,suffix,response.getOutputStream());
    }
    @RequestMapping("/productmanage")
    public  String showProductListPage(){
        return "shop/productList";
    }
    @RequestMapping("productoperation")
    public  String showProductOperationPage(){
        return "shop/productOperation";
    }
}
