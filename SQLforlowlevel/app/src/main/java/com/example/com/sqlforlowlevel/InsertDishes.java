package com.example.com.sqlforlowlevel;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class InsertDishes extends AppCompatActivity {
    Uri uri;
    private  MyDatabase dbhelper1;
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu .toolbar1,menu  ) ;
        return true;
    }
    EditText re_name;
    EditText re_price;
    Button button ;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_dishes);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar2) ;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar .setDisplayHomeAsUpEnabled(true ) ;
        }
        Intent intent =getIntent() ;
        final int id=intent .getExtras().getInt("id") ;
        Button button = (Button)findViewById(R.id.b02);
        button.setText("选择图片");
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                        /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                        /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                        /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
            }
        });
        dbhelper1=new MyDatabase(this,"MyMeiTuan.db",null,8) ;
        re_name=(EditText )findViewById(R.id.re_name1) ;
        re_price=(EditText )findViewById(R.id.re_price1) ;
        ImageView imageView = (ImageView) findViewById(R.id.iv02);
        imageView.setImageResource(R.drawable.dishes);
        button =(Button)findViewById(R.id.insert2) ;
        button .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//判断不能输入为空
                if (TextUtils.isEmpty(re_name.getText())) {
                    Toast.makeText(InsertDishes.this, "请输入饭店名称！", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(re_price.getText())) {
                    Toast.makeText(InsertDishes.this, "请输入菜品价格！", Toast.LENGTH_SHORT).show();
                }
                else{
                String name=re_name.getText().toString();
                String price=re_price .getText() .toString() ;
                    String uri1="1" ;
                    Double price1=Double .parseDouble(price) ;
                    if(!(price1 !=0))//菜品价格不能为0
                        Toast.makeText(InsertDishes.this, "请输入菜品价格！", Toast.LENGTH_SHORT).show();
                    else{
                    if(uri==null)
                        uri1="1" ;
                    else
                        uri1=uri .toString() ;
                SQLiteDatabase db=dbhelper1 .getReadableDatabase();
                ContentValues values = new ContentValues();
                values .put("name",name) ;
                values.put("price",price ) ;
                values.put("dishesID",id) ;
                        values .put("image",uri1) ;
                db.insert("Dishes",null,values ) ;
                Intent intent =new Intent() ;
                intent .setClass(InsertDishes .this,Dishes .class ) ;
                intent .putExtra("id",id);//插入新菜品信息
                InsertDishes .this.startActivity(intent ) ;}
            }}
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//设置图片并且保存图片路径
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                ImageView imageView = (ImageView) findViewById(R.id.iv02);
                                /* 将Bitmap设定到ImageView */
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
}}
