package com.example.com.sqlforlowlevel;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private   MyDatabase dbhelper;
    private List<Resturant > MyResturant=new ArrayList<>();
    SwipeRefreshLayout swipeRefresh;
    ResturantAdapter resturantAdapter;
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu .toolbar,menu  ) ;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item .getItemId() ){
            case R.id.Insert://跳转到添加饭店
                Intent intent=new Intent();
                intent .setClass(MainActivity .this , InsertResturant.class );
                MainActivity .this  .startActivity(intent);
                break;
            case R.id.have_not_paid://跳转到未支付订单
                Intent intent1=new Intent();
                intent1 .setClass(MainActivity .this , Order.class );
                MainActivity .this  .startActivity(intent1);
                break;
            case R.id.have_paid://跳转到已支付订单
                Intent intent2=new Intent();
                intent2 .setClass(MainActivity .this , OrderFinish.class );
                MainActivity .this  .startActivity(intent2);
                break;
            default :
                break;
        }
        return true;
    }
    @Override
    protected void onStart(){
        super .onStart() ;
        input() ;
        setUI();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        dbhelper=new MyDatabase(this,"MyMeiTuan.db",null,8) ;
        input() ;//数据库读取信息
        setUI();//adapter绑定
        swipeRefresh=(SwipeRefreshLayout)  findViewById(R.id.swipe_refresh );//下拉刷新及后续操作
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
                                input() ;
                                setUI();
                                resturantAdapter.notifyDataSetChanged();
                                swipeRefresh.setRefreshing(false );
                            }
                        });
                    }
                }).start();
            }
        });
    }
    public void setUI(){
        RecyclerView recyclerView =(RecyclerView ) findViewById(R.id.recyclerview);
        StaggeredGridLayoutManager LayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL) ;
        recyclerView .setLayoutManager(LayoutManager );
        resturantAdapter = new ResturantAdapter(MyResturant);
        recyclerView.setAdapter(resturantAdapter);
        resturantAdapter.setOnItemClickListener(new ResturantAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {//点击监听事件，跳转到该饭店的菜品详情
                Resturant  resturant =MyResturant.get(position );
                Intent intent =new Intent(MainActivity  .this,Dishes.class );
                intent .putExtra("id",resturant.getId()) ;//传入饭店的ID
                MainActivity .this.startActivity(intent);
            }
        });
    }
    public void input(){
        SQLiteDatabase db=dbhelper .getWritableDatabase();
        MyResturant=new ArrayList<>();
        Cursor cursor = db.query("Resturant", null, null, null, null, null, null);//选出所以的饭店信息
        if (cursor.moveToFirst()) {
            do {
                String adress = cursor.getString(cursor.getColumnIndex("adress"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String uri = cursor.getString(cursor.getColumnIndex("uri"));
                Resturant resturant =new Resturant(name,adress,id,uri) ;
                MyResturant .add(resturant ) ;//选出来的信息填入list
            } while (cursor.moveToNext());
        }
    }
}

