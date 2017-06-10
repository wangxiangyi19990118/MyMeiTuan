package com.example.com.sqlforlowlevel;

/**
 * Created by 定不负相思懿 on 2017/6/2.
 */

public class DishesClass {
        private String name;
        private double price;
        private String image ;
        DishesClass (String name,double price,String image){
            this.name =name;
            this.price =price ;
            this.image=image ;
        }
        public String getName (){
            return name ;
        }
        public double getPrice (){
            return price ;
        }
        public String  getImage (){
            return image ;
        }
}