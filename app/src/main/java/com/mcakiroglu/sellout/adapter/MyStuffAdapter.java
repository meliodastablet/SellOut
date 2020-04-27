package com.mcakiroglu.sellout.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mcakiroglu.sellout.activities.DisplayStuff;
import com.mcakiroglu.sellout.activities.Property;
import com.mcakiroglu.sellout.R;

import java.util.ArrayList;
import java.util.List;

public class MyStuffAdapter extends RecyclerView.Adapter<MyStuffAdapter.MyViewHolder> {

    Context context;
    ArrayList<Property> data;

    public MyStuffAdapter(Context context, List<Property> data) {
        this.context = context;
        this.data =(ArrayList<Property>) data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mystuff_rwlayout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //BIND DATA
        final Property property = data.get(position);

        String s;
        if(property.getStatus().equals("0"))
            s = "Satışta";
        else if(property.getStatus().equals("1"))
            s = "Satıldı";
        else
            s = "İptal edildi";

        holder.t1.setText(s);
        holder.t2.setText("Durum:");
        holder.t3.setText(property.getCategory());
        holder.t4.setText("Kategori:");
        holder.t5.setText(property.getDate());
        holder.t6.setText("Tarih:");
        holder.t7.setText(property.getPrice() + "TL");
        holder.t8.setText("Fiyat:");


        Glide.with(context).load(property.getImage1()).into(holder.iw);

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayStuff.class);
                intent.putExtra("prop",property);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iw;
        TextView t1,t2,t3,t4,t5,t6,t7,t8;
        LinearLayout ll;

        MyViewHolder(View viewItem){
            super(viewItem);
            iw = viewItem.findViewById(R.id.ilanphoto);
            t1 = viewItem.findViewById(R.id.status);
            t2 = viewItem.findViewById(R.id.statustitle);
            t3 = viewItem.findViewById(R.id.category);
            t4 = viewItem.findViewById(R.id.categorytitle);
            t5 = viewItem.findViewById(R.id.date);
            t6 = viewItem.findViewById(R.id.datetitle);
            t7 = viewItem.findViewById(R.id.price);
            t8 = viewItem.findViewById(R.id.pricetitle);
            ll = viewItem.findViewById(R.id.rwlayout);


        }
    }



}
