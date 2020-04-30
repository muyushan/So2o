package com.sane.o2o.enums;

public enum  ShopStateEnum {
    CHECK(0,"审核中"),
    OFFLINE(-1,"非法店铺"),
    SUCCESS(1,"操作成功"),
    NULL_SHOP(-10002,"店铺信息为空"),
    PASS(2,"通过认证"),
    INNER_ERROR(-10001,"内部系统错误");
    private int state;
    private String stateInfo;
    private ShopStateEnum(int state,String stateInfo){
        this.state=state;
        this.stateInfo=stateInfo;
    }

    public static ShopStateEnum stateOf(int state){
        for(ShopStateEnum stateEnum:values()){
            if(stateEnum.state==state){
                return stateEnum;
            }
        }
        return  null;
    }

    public int getState() {
        return state;
    }


    public String getStateInfo() {
        return stateInfo;
    }

}
