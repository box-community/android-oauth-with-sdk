package com.example.boxjavasdkinandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.box.sdk.BoxAPIConnection;

import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(view -> launchBoxTab());
    }

    private void launchBoxTab() {
        List<String> scopes = Collections.singletonList("root_readonly");
        URI redirectUri = URI.create("oauth://boxinandroid");
        String state = UUID.randomUUID().toString();
        MainActivity.this.getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putString("OAuthState", state)
                .apply();
        URL authURL = BoxAPIConnection.getAuthorizationURL(getString(R.string.client_id),
                redirectUri, state, scopes);
        new CustomTabsIntent.Builder().build().launchUrl(this, Uri.parse(authURL.toString()));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            Uri callback = intent.getData();
            String callbackState = callback.getQueryParameter("state");
            String savedState = MainActivity.this.getPreferences(Context.MODE_PRIVATE)
                    .getString("OAuthState", "");

            if (!Objects.equals(callbackState, savedState)) {
                Toast.makeText(MainActivity.this, "State validation failed!", Toast.LENGTH_LONG).show();
                return;
            }

            String code = callback.getQueryParameter("code");
            Intent itemsIntent = new Intent(MainActivity.this, ItemsActivity.class);
            itemsIntent.putExtra("code", code);
            MainActivity.this.startActivity(itemsIntent);
        }
    }
}