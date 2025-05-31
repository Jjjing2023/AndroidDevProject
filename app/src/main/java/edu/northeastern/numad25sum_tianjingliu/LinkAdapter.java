package edu.northeastern.numad25sum_tianjingliu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LinkAdapter extends RecyclerView.Adapter<LinkViewHolder> {
    private List<LinkItem> links;
    private final Context context;

    public LinkAdapter(List<LinkItem> links, Context context){
        this.links = links;
        this.context = context;
    }

    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create an instance of the viewholder by passing it the layout inflated as view and no root
        return new LinkViewHolder(LayoutInflater.from(context).inflate(R.layout.item_link, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder holder, int position) {
        LinkItem currentItem = links.get(position);
        holder.name.setText(currentItem.getName());
        holder.url.setText(currentItem.getUrl());

        //make the url item clickable
        holder.url.setOnClickListener(view->{
            String url = currentItem.getUrl();

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        });

    }

    @Override
    public int getItemCount() {
        return links.size();
    }

}
