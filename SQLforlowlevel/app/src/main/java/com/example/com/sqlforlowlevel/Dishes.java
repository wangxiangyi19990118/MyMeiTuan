package com.example.com.sqlforlowlevel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Dishes extends AppCompatActivity {
    public MyDatabase dbhelper;
    SwipeRefreshLayout swipeRefresh;
    public  String detials="";
    public double price1;
    private List<DishesClass> MyDishes = new ArrayList<>();
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu .toolbar,menu  ) ;
        return true;
    }
    int id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar .setDisplayHomeAsUpEnabled(true ) ;//返回键出现
        }
        final Intent intent = getIntent();
        id = intent.getExtras().getInt("id");
        FloatingActionButton button1 = (FloatingActionButton) findViewById(R.id.check);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ifinsert() )
                    Toast.makeText(Dishes .this,"请选择商品！",Toast .LENGTH_SHORT ).show() ;//判断是否选择了商品
                else{//如果选择了菜品，生成订单
                    input();
                    input2();
                Intent intent1 = new Intent();
                intent1.setClass(Dishes.this, Order.class);
                Dishes.this.startActivity(intent1);
            }}
        });
        dbhelper = new MyDatabase(this, "MyMeiTuan.db", null, 8);
        input(id);//数据库读取信息
        setUI();//adapter绑定
        swipeRefresh=(SwipeRefreshLayout)  findViewById(R.id.swipe_refresh1 );//下拉刷新及后续操作
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
                                input(id) ;
                                setUI();
                                swipeRefresh.setRefreshing(false );
                            }
                        });
                    }
                }).start();
            }
        });
    }
    public void setUI(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
        StaggeredGridLayoutManager LayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(LayoutManager);
        final DishesAdapter dishesAdapter = new DishesAdapter(MyDishes);
        recyclerView.setAdapter(dishesAdapter);
    }
    public void input(int id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        MyDishes=new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from Dishes where dishesID=? ", new String[]{String.valueOf(id)});//按照时间顺序查找符合条件的
        if (cursor.moveToFirst()) {
            do {
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                DishesClass dishesClass = new DishesClass(name, price,image );
                MyDishes.add(dishesClass);
            } while (cursor.moveToNext());
        }
    }
    public boolean  ifinsert() {//查看是否选择了商品（如果总价是0零则认为未选择商品
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        double tolmoney = 0;
        Cursor cursor = db.rawQuery("select * from Basket", null);
        if (cursor.moveToFirst()) {
            do {
                double price = cursor.getDouble(cursor.getColumnIndex("tolmoney"));
                tolmoney +=price ;
            } while (cursor.moveToNext());
        }
        if(tolmoney!=0)
            return true ;
        else
            return false ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                this.finish();
                return false;
            case R.id.Insert:
                Intent intent1 = new Intent();
                intent1.putExtra("id", id);
                intent1.setClass(Dishes.this, InsertDishes.class);
                Dishes.this.startActivity(intent1);
                break;
            case R.id.have_not_paid:
                Intent intent3=new Intent();
                intent3 .setClass(Dishes.this, Order.class);
                Dishes.this  .startActivity(intent3);
                break;
            case R.id.have_paid:
                Intent intent2=new Intent();
                intent2 .setClass(Dishes.this, OrderFinish.class);
                Dishes .this  .startActivity(intent2);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    public void input() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query("Basket", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                double price = cursor.getDouble(cursor.getColumnIndex("tolmoney"));
                String detial = cursor.getString(cursor.getColumnIndex("detial"));
                detials=detials +detial +",";
                price1 =price1 +price ;
            } while (cursor.moveToNext());
        }
        cursor .close() ;
        db.execSQL("drop table if exists Basket") ;
        final String CREATE_BASKET="create table Basket("
                +"id integer primary key autoincrement,"
                +"detial text,"
                +"tolmoney text)";
        db.execSQL(CREATE_BASKET);
    }
    public void input2(){
    SQLiteDatabase db = dbhelper.getWritableDatabase();
    ContentValues values = new ContentValues();
    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
    String str = formatter.format(curDate);
        values .put("detial",detials ) ;
        values.put("tolprice",price1 ) ;
        values .put("paid",0) ;
        values .put("time", String.valueOf(curDate)) ;
        db.insert("List",null,values ) ;
        values .clear() ;}
}