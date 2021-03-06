package com.sane.o2o.enums;

public enum ProductStateEnum {

    SUCCESS(1,"操作成功"),
    INNER_ERROR(-10001,"内部系统错误"),
    EMPTY_LIST(-10002,"插入列表长度不能为空");
    private int state;
    private String stateInfo;
    private ProductStateEnum(int state, String stateInfo){
        this.state=state;
        this.stateInfo=stateInfo;
    }

    public static ProductStateEnum stateOf(int state){
        for(ProductStateEnum stateEnum:values()){
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
