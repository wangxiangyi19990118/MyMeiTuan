package com.example.com.sqlforlowlevel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 定不负相思懿 on 2017/6/1.
 */

public class MyDatabase extends SQLiteOpenHelper {//生成四张表，具体作用见设计技术文档
    public static final String CREATE_RESTURANT="create table Resturant("
            +"id integer primary key autoincrement,"
            +"name text,"
            +"uri text,"
            +"adress text)";
    public static final String CREATE_DISTS="create table Dishes("
            +"id integer primary key autoincrement,"
            +"name text,"
            +"price real,"
            +"dishesID integer,"
            +"image text,"
            +"foreign key(dishesID) references Resturant(id) on delete cascade)";
    public static final String CREATE_BASKET="create table Basket("
            +"id integer primary key autoincrement,"
            +"detial text,"
            +"tolmoney text)";
    public static final String CREATE_LIST="create table List("
            +"id integer primary key autoincrement,"
            +"time text,"
            +"number text,"
            +"tolprice real,"
            +"detial text,"
            +"paid integer)";
    private Context mcontext;
    public MyDatabase (Context context , String name, SQLiteDatabase.CursorFactory  factory , int version){
        super(context ,name,factory ,version );
        mcontext =context ;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RESTURANT) ;
        db.execSQL(CREATE_LIST) ;
        db.execSQL(CREATE_DISTS);
        db.execSQL(CREATE_BASKET);
        Toast.makeText(mcontext ,"Creat Susseful!",Toast.LENGTH_LONG).show(); ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Resturant") ;
        db.execSQL("drop table if exists Dishes") ;
        db.execSQL("drop table if exists List") ;
        db.execSQL("drop table if exists Basket") ;
        onCreate(db) ;
    }
    @Override
    public void onOpen(SQLiteDatabase db){
        db.execSQL("pragma foreign_keys=ON;") ;
    }
}

