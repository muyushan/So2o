package com.sane.o2o.service.impl;

import com.sane.o2o.dao.ProductDao;
import com.sane.o2o.dto.ProductExecution;
import com.sane.o2o.entity.Product;
import com.sane.o2o.entity.ProductImg;
import com.sane.o2o.entity.Shop;
import com.sane.o2o.enums.ProductStateEnum;
import com.sane.o2o.exceptions.ProductOperationException;
import com.sane.o2o.service.ProductService;
import com.sane.o2o.util.ImgUtil;
import com.sane.o2o.util.PageUtil;
import com.sane.o2o.util.PathUtil;
import org.apache.commons.collections.list.PredicatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductExecution addProduct(Product product, MultipartFile image, List<MultipartFile> imageList) {

        ProductExecution productExecution=new ProductExecution(ProductStateEnum.SUCCESS);
        /**
         * 1、处理图片水印，保存图片并将路径返回
         */
        if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null){
            product.setCreateTime(new Date());
            product.setEnableStatus(1);
        }else{
            productExecution.setState(ProductStateEnum.INNER_ERROR.getState());
            productExecution.setStateInfo("产品信息和所属店铺信息都不允许为空");
            return productExecution;
        }
        if(StringUtils.isEmpty(product.getProductName())){
            productExecution.setState(ProductStateEnum.INNER_ERROR.getState());
            productExecution.setStateInfo("产品名称不允许为空");
            return productExecution;
        }
        if(StringUtils.isEmpty(product.getNormalPrice())){
            productExecution.setState(ProductStateEnum.INNER_ERROR.getState());
            productExecution.setStateInfo("产品现价不能为空");
            return productExecution;
        }
        if(image!=null){
            try {
                String path=saveImage(product,image);
                product.setImgAddr(path);
            } catch (IOException e) {
                throw new ProductOperationException("保存图片失败，请重新上传");
            }
        }
        try{
            int count=productDao.insertProduct(product);
            if(count<=0){
                throw new ProductOperationException("添加商品信息失败");
            }
        }catch (Exception ex){
            throw new ProductOperationException("添加商品信息失败:"+ex.getMessage());
        }

        if(!CollectionUtils.isEmpty(imageList)){
            try {
                addProductImageList(imageList,product);
            } catch (IOException e) {
                throw new ProductOperationException("保存图片信息失败，请重新上传");
            }
        }

        return new ProductExecution(ProductStateEnum.SUCCESS);
    }

    @Override
    public ProductExecution modifyProduct(Product product, MultipartFile image, List<MultipartFile> imageList) {
        ProductExecution productExecution=new ProductExecution(ProductStateEnum.SUCCESS);
        if(product.getProductId()==null){
            productExecution.setState(ProductStateEnum.INNER_ERROR.getState());
            productExecution.setStateInfo("编辑的商品信息ID不能为空");
            return productExecution;
        }
        /**
         * 1、处理图片水印，保存图片并将路径返回
         */
        Product tempProduct=productDao.queryProductById(product.getProductId());
        if(tempProduct==null){
            productExecution.setState(ProductStateEnum.INNER_ERROR.getState());
            productExecution.setStateInfo("根据传入的商品ID没有查询到对应的商品信息");
            return productExecution;
        }
        if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null){
            product.setLastEditTime(new Date());
        }else{
            productExecution.setState(ProductStateEnum.INNER_ERROR.getState());
            productExecution.setStateInfo("产品信息和所属店铺信息都不允许为空");
            return productExecution;
        }
        if(StringUtils.isEmpty(product.getProductName())){
            productExecution.setState(ProductStateEnum.INNER_ERROR.getState());
            productExecution.setStateInfo("产品名称不允许为空");
            return productExecution;
        }
        if(StringUtils.isEmpty(product.getNormalPrice())){
            productExecution.setState(ProductStateEnum.INNER_ERROR.getState());
            productExecution.setStateInfo("产品现价不能为空");
            return productExecution;
        }

        if(image!=null){
            try {
                String oldImgPath=tempProduct.getImgAddr();
                String path=saveImage(product,image);
                product.setImgAddr(path);
                //删除旧的商品缩略图
                ImgUtil.deleteFileOrPath(oldImgPath);

            } catch (IOException e) {
                throw new ProductOperationException("保存图片失败，请重新上传");
            }
        }
        try{
            int count=productDao.modifyProduct(product);
            if(count<=0){
                throw new ProductOperationException("修改商品信息失败");
            }
        }catch (Exception ex){
            throw new ProductOperationException("修改商品信息失败:"+ex.getMessage());
        }

        if(!CollectionUtils.isEmpty(imageList)){
            try {
                addProductImageList(imageList,product);
            } catch (IOException e) {
                throw new ProductOperationException("保存图片信息失败，请重新上传");
            }
        }

        return new ProductExecution(ProductStateEnum.SUCCESS);
    }

    @Override
    public ProductExecution changeEnableStatus(Product product) {
        ProductExecution productExecution=new ProductExecution(ProductStateEnum.SUCCESS);

        try{
            int count=productDao.modifyProduct(product);
            if(count<=0){
                throw new ProductOperationException("修改商品上架状态失败");
            }
        }catch (Exception ex){
            throw new ProductOperationException("修改商品上架状态失败:"+ex.getMessage());
        }
        return productExecution;
    }

    private void addProductImageList(List<MultipartFile> imageList, Product product) throws IOException,ProductOperationException {
        List<ProductImg> productImgList=new ArrayList<>();
        for(MultipartFile img:imageList){
            String path=saveImage(product,img);
            ProductImg productImg=new ProductImg();
            productImg.setProductId(product.getProductId());
            productImg.setImgAddr(path);
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }

        if(!CollectionUtils.isEmpty(productImgList)){
           int count= productDao.insertProductImage(productImgList);
           if(count!=productImgList.size()){
               throw new ProductOperationException("保存图片失败，请重新保存");
           }
        }
    }

    private String saveImage(Product product,MultipartFile img) throws IOException {
        String destPath=PathUtil.getShopImagePath(product.getShop().getShopId());
        String path=ImgUtil.generateThumbnail(img,destPath);

        return path;
    }


    @Override
    public ProductExecution quereyProductById(Long productId) {
        ProductExecution productExecution=new ProductExecution(ProductStateEnum.SUCCESS);
        try{
            Product product= productDao.queryProductById(productId);
            productExecution.setProduct(product);
        }catch (Exception ex){
            productExecution.setStateInfo("获取商品信息失败");
            productExecution.setState(ProductStateEnum.INNER_ERROR.getState());

        }
        return productExecution;
    }

    @Override
    public ProductExecution queryProductList(Long shopId,int pageIndex,int pageSize) {
        ProductExecution productExecution=new ProductExecution(ProductStateEnum.SUCCESS);
        int rowIndex=PageUtil.calcRowIndex(pageIndex,pageSize);
        Product product=new Product();
        Shop shop=new Shop();
        shop.setShopId(shopId);
        product.setShop(shop);
       int total=productDao.quereyProductCount(product);
       if(total>0){
          List<Product> productList= productDao.quereyProductList(product,rowIndex,pageSize);
          productExecution.setCount(total);
          productExecution.setProductList(productList);
       }else{
           productExecution.setState(ProductStateEnum.EMPTY_LIST.getState());
           productExecution.setStateInfo("查询结果为空");
       }
        return productExecution;
    }
}
