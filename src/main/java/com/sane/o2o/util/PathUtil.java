package com.sane.o2o.util;

import java.io.File;

public class PathUtil {

    private static  final  String seperator=System.getProperty("file.separator");
    public static String getImageStoreBasePath(){

        String osName=System.getProperty("os.name");
        String userName=System.getProperty("user.name");
        String basePath="";
        if(osName.toLowerCase().startsWith("win")){
            basePath="D:/o2o/image";
        }else{
            basePath="/Users/"+userName+"/o2o/image";
        }

        basePath=basePath.replace("/", seperator);
        return  basePath;
    }

    public static String  getShopImagePath(long shopId){
        String imagePath="/upload/images/item/shop/"+shopId+"/";
        return imagePath.replaceAll("/",seperator);
    }
}
