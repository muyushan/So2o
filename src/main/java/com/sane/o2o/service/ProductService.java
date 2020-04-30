package com.sane.o2o.service;

import com.sane.o2o.dto.ProductExecution;
import com.sane.o2o.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    public ProductExecution addProduct(Product product, MultipartFile image, List<MultipartFile> imageList);
    public ProductExecution modifyProduct(Product product, MultipartFile image, List<MultipartFile> imageList);
    public ProductExecution changeEnableStatus(Product product);
    public ProductExecution quereyProductById(Long productId);
    public ProductExecution queryProductList(Long shopId,int pageIndex,int pageSize);

}
