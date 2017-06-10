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

public class InsertResturant extends AppCompatActivity  {
    private  MyDatabase dbhelper1;
    EditText re_name;
    EditText re_adress;
    Button button ;
    Uri uri;
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu .toolbar1,menu  ) ;
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_resturant);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar1) ;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar .setDisplayHomeAsUpEnabled(true ) ;
        }
        Button button = (Button)findViewById(R.id.b01);
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
        re_name=(EditText )findViewById(R.id.re_name) ;
        re_adress=(EditText )findViewById(R.id.re_adress) ;
        ImageView imageView = (ImageView) findViewById(R.id.iv01);
        imageView.setImageResource(R.drawable.imageshousi);
        button =(Button)findViewById(R.id.insert1) ;
        button .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(re_name.getText())) {//饭店名称不为空
                    Toast.makeText(InsertResturant.this, "请输入饭店名称！", Toast.LENGTH_SHORT).show();
                } else {
                String name=re_name.getText().toString();
                String adress=re_adress .getText() .toString() ;
                    String uri1="1" ;
                    if(uri==null)
                        uri1="1" ;
                    else
                        uri1=uri .toString() ;
                    SQLiteDatabase db=dbhelper1 .getReadableDatabase();
                ContentValues values = new ContentValues();
                values .put("name",name) ;
                values.put("adress",adress ) ;
                values .put("uri",uri1) ;
                db.insert("Resturant",null,values ) ;
                values .clear() ;
                Intent intent =new Intent() ;
                intent .setClass(InsertResturant .this,MainActivity .class ) ;
                InsertResturant .this.startActivity(intent ) ;
            }}
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//保存图片路径，显示
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                ImageView imageView = (ImageView) findViewById(R.id.iv01);
                                /* 将Bitmap设定到ImageView */
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
}
