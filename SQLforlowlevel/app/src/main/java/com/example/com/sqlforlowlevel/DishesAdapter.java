package com.example.com.sqlforlowlevel;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 定不负相思懿 on 2017/6/2.
 */

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.ViewHolder >implements View.OnClickListener{
    private List<DishesClass> MyDishes=new ArrayList<>();

    public DishesAdapter (List<DishesClass> resturants ){
        MyDishes=resturants;
    }
    private ResturantAdapter.OnItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(ResturantAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        View Dishesview;
        Context context1;
        ImageView image;
        Button bt01;
        Button bt02 ;
        TextView edt;
        int num;
        public ViewHolder(View view) {
            super(view);
            Dishesview=view;
            name =(TextView ) view.findViewById(R.id.DishName) ;
            price=(TextView)view .findViewById(R.id.DishPrice) ;
            image =(ImageView)  view .findViewById(R.id.dishesimage) ;
            bt01=(Button)  view .findViewById(R.id.subbt ) ;
            bt02=(Button)  view.findViewById(R.id.addbt ) ;
            edt=(TextView)  view.findViewById(R.id.edt ) ;
            //+和-的点击事件
            bt01.setOnClickListener(new OnButtonClickListener());
            bt02.setOnClickListener(new OnButtonClickListener());
            //中间显示文字的改变监听
            edt.addTextChangedListener(new OnTextChangeListener());
            Button button =(Button )view.findViewById(R.id.intoBaeket) ;
            final MyDatabase dbhelper;
            dbhelper = new MyDatabase(Dishesview .getContext() ,"MyMeiTuan.db",null,8);
            button .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name1=name.getText().toString();
                    String price1=price .getText() .toString() ;
                    SQLiteDatabase db=dbhelper .getReadableDatabase();
                    ContentValues values = new ContentValues();
                    if(num!=0){
                    values .put("detial",name1+"*"+num) ;
                    values.put("tolmoney",String .valueOf(Double .parseDouble(price1)*num) ) ;
                    db.insert("Basket",null,values ) ;
                    values .clear() ;
                    Toast .makeText(Dishesview .getContext() ,"添加成功！",Toast .LENGTH_SHORT ).show() ;
                }
                else
                        Toast .makeText(Dishesview .getContext() ,"数量不能为0！",Toast .LENGTH_SHORT ).show() ;
                }
            }) ;
        }
        class OnButtonClickListener implements View.OnClickListener
        {
            @Override
            public void onClick(View v)
            {
                //得到输入框里的数字
                String numString = edt.getText().toString();
                //进行判断为空或是没文字设置为0
                if (numString == null || numString.equals(""))
                {
                    num = 0;
                    edt.setText("0");
                } else{
                    //当点击-的时候一次递减（num--）
                    if (v.getId()==R.id.subbt)
                    {
                        //判断（大于0的才能往下减）
                        if (++num < 0)  //先加，再判断
                        {
                            num--;
                        } else
                        {
                            edt.setText(String.valueOf(num));
                        }
                    } else if (v.getId() ==R.id.addbt )
                    {
                        //判断（自减限制数不小于0）
                        if (--num < 0)  //先减，再判断
                        {
                            num++;
                        } else
                        {
                            edt.setText(String.valueOf(num));
                        }
                    }
                }
            }
        }
        class OnTextChangeListener implements TextWatcher {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String numString = s.toString();
                if (numString == null || numString.equals("")) {
                    num = 0;
                } else {
                    int numInt = Integer.parseInt(numString);
                    if (numInt < 0) {
                    } else {
                        //设置EditText光标位置 为文本末端
                        num = numInt;

                    }
                }
            }
        }
    }

    @Override
    public DishesAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent .getContext() ).inflate(R.layout.dishes_recyclerview,parent ,false ) ;
        DishesAdapter.ViewHolder holder=new DishesAdapter.ViewHolder(view );
        holder.context1=parent .getContext() ;
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DishesClass  dishesClass=MyDishes.get(position );
        holder.itemView.setTag(position);
        holder .name .setText(dishesClass .getName() ) ;
        holder .price.setText(String .valueOf(dishesClass .getPrice())) ;
        if(dishesClass .getImage().equals("1"))
            Glide.with(holder .context1).load(R.drawable .dishes  ).asBitmap().into(holder.image);
        else
            Glide.with(holder .context1).load(dishesClass .getImage() ).asBitmap().error(R.drawable .dishes ) .into(holder.image);
    }
    @Override
    public int getItemCount() {
        return MyDishes .size() ;
    }
    @Override
    public void onClick(View v) {
        if (this.mOnItemClickListener != null) {
            this.mOnItemClickListener.onItemClick(v,(int)v.getTag() );
        }
    }
}



