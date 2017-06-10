package com.example.com.sqlforlowlevel;

/**
 * Created by 定不负相思懿 on 2017/6/3.
 */

public class OrderClass {
    String detial;
    String time;
    String tolcount;
    String ordernumber;
    OrderClass (String detial ,String time ,String tolcount ,String ordernumber ){
        this.detial =detial ;
        this.time =time ;
        this.tolcount =tolcount ;
        this.ordernumber =ordernumber ;
    }
    public String getDetial (){
        return detial ;
    }
    public String getTolcount (){
        return tolcount;
    }
    public String getTime (){
        return time ;
    }
    public String getOrdernumber (){
        return ordernumber ;
    }
}
