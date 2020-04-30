package com.sane.o2o.util;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class EncryptPropertyPlaceHolderConfiger extends PropertyPlaceholderConfigurer {
    private String[] encryptPropNames={"jdbc.username","jdbc.password"};
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if(isEncryptProp(propertyName)){
            String descryptValue=DesUtil.getDecryptString(propertyValue);
            return  descryptValue;
        }else{
            return  propertyValue;
        }
    }

    private boolean isEncryptProp(String propName){
        return Arrays.asList(encryptPropNames).contains(propName);
    }
}
