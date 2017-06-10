package com.example.com.sqlforlowlevel;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.example.com.sqlforlowlevel.ResturantAdapter.dbhelper;

/**
 * Created by 定不负相思懿 on 2017/6/3.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter .ViewHolder >implements View.OnClickListener{
    private List<OrderClass> MyOrder=new ArrayList<>();
    private static MyDatabase dbhelper;
    public OrderAdapter (List<OrderClass> resturants ){
        MyOrder=resturants;
    }
    private OrderAdapter.OnItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OrderAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView detials;
        TextView tolprice;
        TextView time;
        TextView id;
        Button pay;
        View orderview;
        Context context1;

        public ViewHolder(View view) {
            super(view);
            orderview = view;
            detials = (TextView) view.findViewById(R.id.Orderdetial);
            time = (TextView) view.findViewById(R.id.time);
            tolprice = (TextView) view.findViewById(R.id.OrderTolmoney);
            pay = (Button) view.findViewById(R.id.Pay);
            id=(TextView)  view .findViewById(R.id.id) ;
            pay .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {//点击支付订单，修改表单中的支付信息
                    dbhelper=new MyDatabase(view .getContext(),"MyMeiTuan.db",null,8) ;
                    SQLiteDatabase db=dbhelper.getWritableDatabase() ;
                    ContentValues values =new ContentValues() ;
                    values.put("paid","1") ;
                    String a=id.getText().toString();
                    db.update("List",values ,"id=?",new String[]{id.getText().toString() }) ;
                    Intent intent=new Intent();
                    intent .setClass(view .getContext(),OrderFinish .class) ;//刷新该页面
                    view .getContext() .startActivity(intent ) ;
                }
            }) ;
        }
    }
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent .getContext() ).inflate(R.layout.order_layout,parent ,false ) ;
        OrderAdapter.ViewHolder holder=new OrderAdapter.ViewHolder(view );
        holder.context1=parent .getContext() ;
        view.setOnClickListener(this);
        return holder ;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderClass orderClass=MyOrder .get(position );
        holder.itemView.setTag(position);
        holder .detials .setText(orderClass .getDetial() ) ;
        holder . tolprice .setText(orderClass .getTolcount().toString()  ) ;
        holder .time .setText(orderClass .getTime() );
        holder .id.setText(orderClass .getOrdernumber()) ;
    }
    @Override
    public int getItemCount() {
        return MyOrder .size() ;
    }
    @Override
    public void onClick(View v) {
        if (this.mOnItemClickListener != null) {
            this.mOnItemClickListener.onItemClick(v,(int)v.getTag() );
        }
    }
}


