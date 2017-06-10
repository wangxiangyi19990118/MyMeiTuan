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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 定不负相思懿 on 2017/6/8.
 */

public class OrderFinishAdapter extends RecyclerView.Adapter<OrderFinishAdapter .ViewHolder >implements View.OnClickListener{
    private List<OrderClass> MyOrder=new ArrayList<>();
    private static MyDatabase dbhelper;
    public OrderFinishAdapter(List<OrderClass> resturants ){
        MyOrder=resturants;
    }
    private OrderAdapter.OnItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OrderAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView detials;
        TextView tolprice;
        TextView time;
        TextView id;
        Button delete;
        View orderview;
        Context context1;

        public ViewHolder(View view) {
            super(view);
            orderview = view;
            detials = (TextView) view.findViewById(R.id.Orderdetial1);
            time = (TextView) view.findViewById(R.id.time1);
            tolprice = (TextView) view.findViewById(R.id.OrderTolmoney1);
            delete = (Button) view.findViewById(R.id.Delete);
            id=(TextView)  view .findViewById(R.id.id1) ;
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {//点击可以删除该记录
                    dbhelper=new MyDatabase(view .getContext(),"MyMeiTuan.db",null,8) ;
                    SQLiteDatabase db=dbhelper.getWritableDatabase() ;
                    ContentValues values =new ContentValues() ;
                    String a=id.getText().toString();
                    db.delete("List","id=?",new String[]{id .getText() .toString() });
                    Intent intent=new Intent();
                    intent .setClass(view .getContext(),OrderFinish .class) ;
                    view .getContext() .startActivity(intent ) ;
                }
            }) ;
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent .getContext() ).inflate(R.layout.order_finish,parent ,false ) ;
        OrderFinishAdapter.ViewHolder holder=new OrderFinishAdapter.ViewHolder(view );
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


