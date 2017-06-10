package com.example.com.sqlforlowlevel;

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

/**
 * Created by 定不负相思懿 on 2017/6/1.
 */

public class ResturantAdapter extends RecyclerView.Adapter<ResturantAdapter  .ViewHolder >implements View.OnClickListener{
    private List<Resturant> MyResturant=new ArrayList<>();
    static MyDatabase dbhelper;
    public ResturantAdapter (List<Resturant> resturants ){
        MyResturant=resturants;
    }
    private  OnItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView adress;
        TextView id;
        Button update;
        Button delete;
        View resturantview;
        Context context1;
        ImageView image;
        public ViewHolder(View view) {
            super(view);
            resturantview =view;
            name =(TextView ) view.findViewById(R.id.ResturantName) ;
            id=(TextView)  view .findViewById(R.id.ResturantId) ;
            adress=(TextView)view .findViewById(R.id.ResturantAdress) ;
            update =(Button ) view.findViewById(R.id.update_resturant);
            delete =(Button )view .findViewById(R.id.delete_resturant) ;
            image =(ImageView)  view .findViewById(R.id.resturantimage ) ;
            update .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {//对饭店信息的更改，活动跳转
                    Intent intent=new Intent();
                    intent .setClass(view .getContext(),UpdateResturant .class );
                    intent .putExtra("name",name.getText().toString() );
                    String a=name.getText() .toString() ;
                    intent .putExtra("adress",adress.getText().toString() );
                    intent.putExtra("id",id.getText().toString().toString() );
                    String i=id.getText().toString();
                    view .getContext() .startActivity(intent);
                }
            }) ;
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {//删除该饭店，同时删除该饭店名下的菜品
                    dbhelper=new MyDatabase(view .getContext(),"MyMeiTuan.db",null,8) ;
                    SQLiteDatabase db=dbhelper.getWritableDatabase() ;
                    db.delete("Resturant","id=?",new String[]{id .getText() .toString() });
                    db.delete("Dishes","dishesID=?",new String[]{id .getText() .toString() });
                    Intent intent=new Intent();
                    intent .setClass(view .getContext(),MainActivity .class) ;
                    view .getContext() .startActivity(intent ) ;
                }
            });
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent .getContext() ).inflate(R.layout.resturant_layout,parent ,false ) ;
        ViewHolder holder=new ViewHolder(view );
        holder.context1=parent .getContext() ;
        view.setOnClickListener(this);
        return holder ;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Resturant  restruant1=MyResturant.get(position );
        holder.itemView.setTag(position);
        holder .name.setText(restruant1 .getRe_name() ) ;
        holder .adress .setText(restruant1 .getRe_adress() ) ;
        holder .id.setText(String .valueOf(restruant1 .getId()));
        if(restruant1 .getUri().equals("1"))
            Glide.with(holder .context1).load(R.drawable .imageshousi  ).asBitmap().into(holder.image);
            else
        Glide.with(holder .context1).load(restruant1 .getUri() ).asBitmap().error(R.drawable .imageshousi ) .into(holder.image);
    }
    @Override
    public int getItemCount() {
        return MyResturant .size() ;
    }
    @Override
    public void onClick(View v) {
        if (this.mOnItemClickListener != null) {
            this.mOnItemClickListener.onItemClick(v,(int)v.getTag() );
        }
    }
}


