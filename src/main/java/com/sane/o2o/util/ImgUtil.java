package com.sane.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImgUtil {
    private  static String bathPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    private static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
    private static Random random=new Random();
    public static String generateThumbnail(MultipartFile multipartFile,String targetAddr) throws IOException {
        String realFileName=getRanomFileName();
        String extension=getFileExtension(multipartFile);
        makDirPath(targetAddr);
        String relativeAddr=targetAddr+realFileName+extension;
        File dest=new File(PathUtil.getImageStoreBasePath()+relativeAddr);
        Thumbnails.of(multipartFile.getInputStream()).size(200,200).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(bathPath + "/water.jpeg")), 0.5f).outputQuality(0.8f).toFile(dest);
        return  relativeAddr;

    }

    private static String getRanomFileName(){
         Integer randumNum=random.nextInt(89999)+1000;
         String nowStr=simpleDateFormat.format(new Date());
         return  nowStr+randumNum;
    }

    private static String getFileExtension(MultipartFile multipartFile){
        String oriName=multipartFile.getOriginalFilename();
        return  oriName.substring(oriName.lastIndexOf("."));
    }

    private  static void makDirPath(String dir){
        String realFileParentPath=PathUtil.getImageStoreBasePath()+dir;
        File file=new File(realFileParentPath);
        if(!file.exists()){
            file.mkdirs();
        }else{
            if(file.isFile()){
                file.delete();
            }

        }
    }

    public static void deleteFileOrPath(String storePath){
        File fileOrPath=new File(PathUtil.getImageStoreBasePath()+storePath);
        if(fileOrPath.exists()){
            if(fileOrPath.isDirectory()){
                File[] files=fileOrPath.listFiles();
                for(int i=0;i<files.length;i++){
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }

    }
}
