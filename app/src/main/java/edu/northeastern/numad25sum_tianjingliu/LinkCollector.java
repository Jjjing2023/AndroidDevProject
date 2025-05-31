package edu.northeastern.numad25sum_tianjingliu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class LinkCollector extends AppCompatActivity {
    private List<LinkItem> links = new ArrayList<>();
    private RecyclerView linkRecyclerView;
    private LinkAdapter linkAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_collector);


        linkRecyclerView = findViewById(R.id.link_recyclerView);
        linkRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinkViewModel linkViewModel = new ViewModelProvider(this).get(LinkViewModel.class);
        links = linkViewModel.getLinks();

        linkAdapter = new LinkAdapter(links, this);
        linkRecyclerView.setAdapter(linkAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showAddLinkDialog());}

    private void showAddLinkDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_link, null);
        dialogBuilder.setView(dialogView);

        EditText editName = dialogView.findViewById(R.id.edit_name);
        EditText editUrl = dialogView.findViewById(R.id.edit_url);

        dialogBuilder.setTitle("Add New Link");
        dialogBuilder.setPositiveButton("Add", (dialog, which) -> {
            String name = editName.getText().toString();
            String url = editUrl.getText().toString();

            links.add(new LinkItem(name, url));
            linkAdapter.notifyItemInserted(links.size() - 1);

            // show snackbar after addition
            Snackbar.make(findViewById(R.id.link_collector_root),
                    "Added: " + name, Snackbar.LENGTH_SHORT)
                    .setAnchorView(findViewById(R.id.fab))
                    .show();

        });
        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }



}