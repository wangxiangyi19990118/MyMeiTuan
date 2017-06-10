package com.example.com.sqlforlowlevel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Order extends AppCompatActivity {
    public  MyDatabase dbhelper;
    SwipeRefreshLayout swipeRefresh;
    private List<OrderClass  > MyOrder=new ArrayList<>();
    private List<OrderClass  > MyOrder1=new ArrayList<>();
    public  String detials="";
    public double price1;
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu .toolbar1,menu  ) ;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar1) ;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar .setDisplayHomeAsUpEnabled(true ) ;
        }
        dbhelper=new MyDatabase(this,"MyMeiTuan.db",null,8) ;
        input1() ;
        RecyclerView recyclerView =(RecyclerView ) findViewById(R.id.recyclerview2);
        StaggeredGridLayoutManager LayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL) ;
        recyclerView .setLayoutManager(LayoutManager );
        final  OrderAdapter  orderAdapter  = new OrderAdapter(MyOrder);
        recyclerView.setAdapter(orderAdapter);
        swipeRefresh=(SwipeRefreshLayout)  findViewById(R.id.swipe_refresh2 );//下拉刷新及后续操作
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(750);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SQLiteDatabase db = dbhelper.getWritableDatabase();
                                Cursor cursor = db.rawQuery("select *from list where paid="+"0",null  ) ;
                                if (cursor.moveToFirst()) {
                                    do {
                                        double price = cursor.getDouble(cursor.getColumnIndex("tolprice"));
                                        String detial = cursor.getString(cursor.getColumnIndex("detial"));
                                        String time= cursor.getString(cursor.getColumnIndex("time"));
                                        String orderid= cursor.getString(cursor.getColumnIndex("number"));
                                        MyOrder .add(new OrderClass(detial,time,String .valueOf(price) ,orderid )) ;
                                    } while (cursor.moveToNext());
                                }
                                cursor .close() ;
                                orderAdapter .notifyDataSetChanged();
                                swipeRefresh.setRefreshing(false );
                            }
                        });
                    }
                }).start();
            }
        });
    }
    public void input1(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery("select *from list where paid="+"0"+" order by time DESC",null ) ;//选择未支付订单,按照时间排序
       if (cursor.moveToFirst()) {
            do {
                double price = cursor.getDouble(cursor.getColumnIndex("tolprice"));
                String detial = cursor.getString(cursor.getColumnIndex("detial"));
                String time= cursor.getString(cursor.getColumnIndex("time"));
                String orderid= cursor.getString(cursor.getColumnIndex("id"));
               MyOrder .add(new OrderClass(detial,time,String .valueOf(price),orderid  ) ) ;
            } while (cursor.moveToNext());
        }
        cursor .close() ;
    }
}
