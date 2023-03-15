package com.example.boxjavasdkinandroid;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String code = extras.getString("code");
            if (!code.isEmpty()) {
                TextView codeTextView = findViewById(R.id.code_text_view);
                codeTextView.setText(code);
            }
        }
    }
}
