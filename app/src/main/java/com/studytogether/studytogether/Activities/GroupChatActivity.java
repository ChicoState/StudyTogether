package com.studytogether.studytogether.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
        String chatGroupName = intent.getExtras().getString("GroupName");
        String chatGroupPlace = intent.getExtras().getString("GroupPlace");
        String chatGroupGoal = intent.getExtras().getString("GroupGoal");
        int chatGroupPosition = intent.getIntExtra("position", 0);
        String groupCreated = timestampToString(getIntent().getExtras().getLong("addedDate"));

        final Toolbar toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.chat_collapsing_toolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.toolbar_text);
        collapsingToolbar.setTitle(chatGroupName);

        final ImageView imageView = findViewById(R.id.chat_backdrop);
        String imageUrl = getIntent().getStringExtra("GroupImg");
        Glide.with(this).load(imageUrl).apply(RequestOptions.centerCropTransform()).into(imageView);

        final AppBarLayout appbarLayout = (AppBarLayout)findViewById(R.id.chat_appbar_layout);
        FloatingActionButton groupDetail = (FloatingActionButton) findViewById(R.id.group_detail_btn);
        // If the floating action button is clicked,
        groupDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupDetailActivity = new Intent(getApplicationContext(), GroupDetailActivity.class);

                // passing data to the GroupDetailActivity
                groupDetailActivity.putExtra("GroupName", chatGroupName);
                groupDetailActivity.putExtra("GroupPlace", chatGroupPlace);
                groupDetailActivity.putExtra("GroupGoal", chatGroupGoal);
                groupDetailActivity.putExtra("GroupImg",imageUrl);
                groupDetailActivity.putExtra("addedDate", groupCreated);
                startActivity(groupDetailActivity);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    // Get server time and convert into String
    private String timestampToString(long time) {

        // Get time
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        // Reform the time and cast into String type
        String date = DateFormat.format("MM-dd-yyyy",calendar).toString();
        return date;


    }

    // Show message to the user
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

}

