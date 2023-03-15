package com.example.boxjavasdkinandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.example.boxjavasdkinandroid.Models.File;
import com.example.boxjavasdkinandroid.Models.Folder;
import com.example.boxjavasdkinandroid.Models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_items);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
        showProgressBar(progressBar, recyclerView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String code = extras.getString("code");
            if (!code.isEmpty()) {
                updateUI(code, progressBar, recyclerView);
            }
            // in case we don't get any code we should display an error
        }
    }

    private void updateUI(String code, ProgressBar progressBar, RecyclerView recyclerView) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            List<Item> items = getFolderItems(code);
            handler.post(() -> {
                recyclerView.setAdapter(new ItemAdapter(items));
                showData(progressBar, recyclerView);
            });
        });
    }

    private void showProgressBar(ProgressBar progressBar, RecyclerView recyclerView) {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showData(ProgressBar progressBar, RecyclerView recyclerView) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private List<Item> getFolderItems(String code) {
        BoxAPIConnection api = new BoxAPIConnection(getString(R.string.client_id),
                getString(R.string.client_secret), code);
        Iterable<BoxItem.Info> boxFolder = new BoxFolder(api, "0")
                .getChildren("name", "modified_by");

        List<Item> items = new ArrayList<>();
        for (BoxItem.Info itemInfo : boxFolder) {
            if (itemInfo instanceof BoxFile.Info) {
                BoxFile.Info fileInfo = (BoxFile.Info) itemInfo;
                File file = new File(fileInfo.getName(), fileInfo.getModifiedBy().getName());
                items.add(file);
            } else if (itemInfo instanceof BoxFolder.Info) {
                BoxFolder.Info folderInfo = (BoxFolder.Info) itemInfo;
                Folder folder = new Folder(folderInfo.getName(), folderInfo.getModifiedBy().getName());
                items.add(folder);
            }
        }
        return items;
    }
}
