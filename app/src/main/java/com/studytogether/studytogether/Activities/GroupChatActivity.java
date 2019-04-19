package com.studytogether.studytogether.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.studytogether.studytogether.R;

import java.util.Calendar;
import java.util.Locale;

public class GroupChatActivity extends AppCompatActivity {

    // Create items
    TextView chatGroupName;
    ImageView chatGroupImage;
    Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the contentView with group_detail
        setContentView(R.layout.activity_group_chat);

        Intent intent = getIntent();
        String chatGroupName = "groupName";
        if(intent.getExtras() == null) {
            //groupName = intent.getExtras().getString("GroupName");
            showMessage("getExtras is null");
        }
        else {
            chatGroupName = intent.getExtras().getString("GroupName");
        }
        chatGroupName = intent.getExtras().getString("GroupName");

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.toolbar_text);
        collapsingToolbar.setTitle(chatGroupName);

        loadBackdrop();


    }

    private void loadBackdrop() {
        final ImageView imageView = findViewById(R.id.backdrop);
        String imageUrl = getIntent().getStringExtra("GroupImg");
        Glide.with(this).load(imageUrl).apply(RequestOptions.centerCropTransform()).into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    // Show message to the user
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }



}

