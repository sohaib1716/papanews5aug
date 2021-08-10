package com.papanews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class savePostAdapter extends RecyclerView.Adapter<savePostAdapter.ViewHolder> {

    private List<savePost_Model> items;
    int cc=-1;
    int[] gbg = { R.drawable.gradientbackground, R.drawable.gradientbackground2, R.drawable.gradientbackgroound3};

    public savePostAdapter(List<savePost_Model> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.save_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(items.get(position));
        int han[]={0,1,2,1,0};
        if(cc>=4){
            cc=1;
        }else{
            cc++;
        }
        holder.CardbgColor.setBackgroundResource(gbg[han[cc]]);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        MaterialTextView title, shortDe, longDe, long_text, news_views,video,sourcetxt;
        ImageView sourcimg;
        RelativeLayout CardbgColor;
        ImageView image;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.item_age);
            long_text = itemView.findViewById(R.id.longText);
            news_views = itemView.findViewById(R.id.views);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.news_title);
            shortDe = itemView.findViewById(R.id.discription);
            longDe = itemView.findViewById(R.id.item_city);
            sourcimg  = itemView.findViewById(R.id.srcimage);
            sourcetxt = itemView.findViewById(R.id.srcName);
            CardbgColor = itemView.findViewById(R.id.CardbgColor);
        }

        void setData(savePost_Model data) {
            Picasso.get().load(data.getImage()).placeholder(R.drawable.sample1)
                    .resize(550, 550)
                    .into(image);

            Picasso.get().load(data.getSourceimage()).placeholder(R.drawable.sample1)
                    .resize(30, 30)
                    .into(sourcimg);

            sourcetxt.setText(data.getSourcename());

            title.setText(data.gettitle());
            shortDe.setText(data.getshortd());
            longDe.setText(data.getlongd());
            news_views.setText(data.getViews());
            long_text.setText(data.getLongText());
            video.setText(data.getVideo());
        }
    }

    public List<savePost_Model> getItems() {
        return items;
    }

    public void setItems(List<savePost_Model> items) {
        this.items = items;
    }
}
