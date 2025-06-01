package edu.northeastern.numad25sum_tianjingliu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

            // url handler
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        });

        holder.itemView.setOnLongClickListener(view->{
            // show a dialog with "edit" and "delete" options
            new AlertDialog.Builder(context)
                    .setTitle("Choose an action")
                    .setItems(new CharSequence[]{"Edit", "Delete"},(dialog, which)->{
                        if(which==0){
                            showEditDialog(position);
                        }else{
                            links.remove(position);
                            notifyItemRemoved(position);
                        }
                    })
                    .show();
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return links.size();
    }

    private void showEditDialog(int position) {
        LinkItem item = links.get(position);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_link, null);
        EditText editName = dialogView.findViewById(R.id.edit_name);
        EditText editUrl = dialogView.findViewById(R.id.edit_url);

        // Pre-fill with current values
        editName.setText(item.getName());
        editUrl.setText(item.getUrl());

        new AlertDialog.Builder(context)
                .setTitle("Edit Link")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = editName.getText().toString();
                    String newUrl = editUrl.getText().toString();

                    item.setName(newName);
                    item.setUrl(newUrl);
                    notifyItemChanged(position);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }



}
