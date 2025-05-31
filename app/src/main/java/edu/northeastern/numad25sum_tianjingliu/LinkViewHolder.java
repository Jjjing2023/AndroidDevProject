package edu.northeastern.numad25sum_tianjingliu;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LinkViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView url;

    public LinkViewHolder(@NonNull View itemView){
        super(itemView);
        this.name=itemView.findViewById(R.id.name);
        this.url=itemView.findViewById(R.id.url);
    }
}
