package com.example.com.sqlforlowlevel;

/**
 * Created by 定不负相思懿 on 2017/6/1.
 */

public class Resturant {
    String re_name;
    String re_adress;
    String uri;
    int id;
    int image=R.drawable .chocolate;
    Resturant (String re_name ,String re_adress, int id,String uri ){
        this.re_name =re_name ;
        this.re_adress =re_adress ;
        this.id=id;
        this.uri=uri;
    }
    public String getRe_name (){
        return re_name ;
    }
    public String getRe_adress (){
        return re_adress ;
    }
    public int getImage (){return image ;}
    public int getId (){
        return id;
    }
    public String getUri(){
        return uri;
    }
}

