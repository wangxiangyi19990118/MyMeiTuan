package com.example.com.sqlforlowlevel;

import android.content.ContentValues;
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

import java.util.ArrayList;
import java.util.List;

public class OrderFinish extends AppCompatActivity {//非常类似于Order，只是选择的信息是paid=1
    public  MyDatabase dbhelper;
    SwipeRefreshLayout swipeRefresh;
    private List<OrderClass  > MyOrder1=new ArrayList<>();
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu .toolbar1,menu  ) ;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_finish);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar5) ;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar .setDisplayHomeAsUpEnabled(true ) ;
        }
        dbhelper=new MyDatabase(this,"MyMeiTuan.db",null,8) ;
        input1() ;
        RecyclerView recyclerView1 =(RecyclerView ) findViewById(R.id.recyclerview4);
        StaggeredGridLayoutManager LayoutManager1=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL) ;
        recyclerView1.setLayoutManager(LayoutManager1 );
        final  OrderFinishAdapter  orderAdapter1  = new OrderFinishAdapter(MyOrder1);
        recyclerView1.setAdapter(orderAdapter1);
        swipeRefresh=(SwipeRefreshLayout)  findViewById(R.id.swipe_refresh5 );//下拉刷新及后续操作
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SQLiteDatabase db = dbhelper.getWritableDatabase();
                                Cursor cursor = db.rawQuery("select *from list where paid="+"1",null  ) ;
                                if (cursor.moveToFirst()) {
                                    do {
                                        double price = cursor.getDouble(cursor.getColumnIndex("tolprice"));
                                        String detial = cursor.getString(cursor.getColumnIndex("detial"));
                                        String time= cursor.getString(cursor.getColumnIndex("time"));
                                        String orderid= cursor.getString(cursor.getColumnIndex("id"));
                                        MyOrder1 .add(new OrderClass(detial,time,String .valueOf(price) ,orderid )) ;
                                    } while (cursor.moveToNext());
                                }
                                cursor .close() ;
                                orderAdapter1 .notifyDataSetChanged();
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
        Cursor cursor1 = db.rawQuery("select *from list where paid="+"1",null  ) ;
        if (cursor1.moveToFirst()) {
            do {
                double price = cursor1.getDouble(cursor1.getColumnIndex("tolprice"));
                String detial = cursor1.getString(cursor1.getColumnIndex("detial"));
                String time= cursor1.getString(cursor1.getColumnIndex("time"));
                String orderid= cursor1.getString(cursor1.getColumnIndex("id"));
                MyOrder1 .add(new OrderClass(detial,time,String .valueOf(price) ,orderid )) ;
            } while (cursor1.moveToNext());
        }
        cursor1 .close() ;
    }
}

