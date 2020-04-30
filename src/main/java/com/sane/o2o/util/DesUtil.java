package com.sane.o2o.util;

import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.SecureRandom;

public class DesUtil {
    private static Key key;
    private static  String KEY_SALT="2Wsx#9Ijn";
    private static String CHARSETNAME="UTF-8";
    private static String ALGORITHM="DES";
    static {
        try {
            //生产DES算法对象
            KeyGenerator keyGenerator=KeyGenerator.getInstance(ALGORITHM);
            //运行SHA1安全策略
            SecureRandom secureRandom=SecureRandom.getInstance("SHA1PRNG");
            //设置秘钥种子
            secureRandom.setSeed(KEY_SALT.getBytes());
            //初始化基于SHA1的算法对象
            keyGenerator.init(secureRandom);
            key=keyGenerator.generateKey();
        }catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
    }

    public static String getEncryptString(String str){
        try {
            BASE64Encoder base64Encoder=new BASE64Encoder();
            //按照UTF-8编码
            byte[] bytes=str.getBytes(CHARSETNAME);
            //获取加密对象
            Cipher cipher=Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            //加密
            byte[] doFinal=cipher.doFinal(bytes);
            return base64Encoder.encode(doFinal);
        } catch (Exception e) {
            throw  new RuntimeException(e.getMessage());

        }
    }

    public static String getDecryptString(String str){
        BASE64Decoder base64Decoder=new BASE64Decoder();
        try {
            byte[] bytes=base64Decoder.decodeBuffer(str);
            Cipher cipher=Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,key);
            byte[] doFinal=cipher.doFinal(bytes);
            return new String(doFinal,CHARSETNAME);
        }catch (Exception ex){
            throw  new RuntimeException(ex.getMessage());

        }
    }

    public static void main(String args[]){
        System.out.println(getEncryptString("root"));
        System.out.println(getDecryptString("4YR65mbD1+c="));

        System.out.println(getEncryptString("1Qaz#0Okm"));
        System.out.println(getDecryptString("mcWsjuuMs6MrfnkytW37XA=="));
    }
}
