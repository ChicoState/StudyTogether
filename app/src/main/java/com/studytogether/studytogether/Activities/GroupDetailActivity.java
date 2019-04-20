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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.studytogether.studytogether.R;

import java.util.Calendar;
import java.util.Locale;

// GroupDetail Activity
public class GroupDetailActivity extends AppCompatActivity {

    // Create items
    TextView detailGroupName, detailGroupPlace, detailGroupGoal, detailGroupAddedDate;
    Button detailChatButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the contentView with group_detail
        setContentView(R.layout.group_detail);


        // Set up items with layouts
        detailGroupName = findViewById(R.id.detail_group_name);
        detailGroupPlace = findViewById(R.id.detail_group_place);
        detailGroupGoal = findViewById(R.id.detail_group_goal);
        detailGroupAddedDate = findViewById(R.id.detail_group_added);

        detailChatButton = findViewById(R.id.chat_btn);


        // Create intent
        Intent intent = getIntent();
        // Get info through intent
        String groupName = intent.getExtras().getString("GroupName");
        String groupPlace = intent.getExtras().getString("GroupPlace");
        String groupGoal = intent.getExtras().getString("GroupGoal");
        String groupCreated = timestampToString(getIntent().getExtras().getLong("addedDate"));


        final Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.detail_collapsing_toolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.toolbar_text);
        collapsingToolbar.setTitle(groupName);

        final ImageView imageView = findViewById(R.id.detail_backdrop);
        String detailGroupImageUrl = getIntent().getStringExtra("GroupImg");
        Glide.with(this).load(detailGroupImageUrl).apply(RequestOptions.centerCropTransform()).into(imageView);


        // Set items with info
        detailGroupName.setText(groupName);
        detailGroupPlace.setText(groupPlace);
        detailGroupGoal.setText(groupGoal);
        detailGroupAddedDate.setText(groupCreated);

        final AppBarLayout appbarLayout = (AppBarLayout)findViewById(R.id.detail_appbar_layout);
        FloatingActionButton quitGroup = (FloatingActionButton) findViewById(R.id.group_quit_btn);
        // If the floating action button is clicked,
        quitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent groupDetailActivity = new Intent(getApplicationContext(), GroupDetailActivity.class);

                //startActivity(groupDetailActivity);
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
