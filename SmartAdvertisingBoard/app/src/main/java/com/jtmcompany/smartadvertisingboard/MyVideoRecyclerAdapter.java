package com.jtmcompany.smartadvertisingboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyVideoRecyclerAdapter extends RecyclerView.Adapter<MyVideoRecyclerAdapter.myViewHolder> {
    List<MyVideo_Model> list=new ArrayList<>();
    myVideoClickListener listener;

    interface myVideoClickListener{
        void onClick(String path);
    }

    public void setMyVieoListener(myVideoClickListener myVideoClickListener){
        this.listener=myVideoClickListener;
    }

    public MyVideoRecyclerAdapter(List<MyVideo_Model> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.myadvertise_item,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {
        final MyVideo_Model model=list.get(position);
        holder.image.setImageBitmap(model.getImg());
        holder.title.setText(model.getTitle());

        holder.play_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(model.getPath());
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        ImageView play_bt;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.advertise_img);
            title=itemView.findViewById(R.id.advertise_title);
            play_bt=itemView.findViewById(R.id.myVideoPlay);
        }
    }

}


