package com.studytogether.studytogether.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.studytogether.studytogether.R;

import java.util.Calendar;
import java.util.Locale;

// GroupDetail Activity
public class GroupDetailActivity extends AppCompatActivity {

    // Create items
    TextView detailGroupName, detailGroupPlace, detailGroupGoal, detailGroupAddedDate;
    ImageView detailGroupImg;
    Button detailChatButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the contentView with group_detail
        setContentView(R.layout.group_detail);

        // Hide appBar
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        // Set up items with layouts
        detailGroupName = findViewById(R.id.detail_group_name);
        detailGroupPlace = findViewById(R.id.detail_group_place);
        detailGroupGoal = findViewById(R.id.detail_group_goal);
        detailGroupImg = findViewById(R.id.detail_group_img);
        detailGroupAddedDate = findViewById(R.id.detail_group_added);

        detailChatButton = findViewById(R.id.chat_btn);


        // Create intent
        Intent intent = getIntent();
        // Get info through intent
        String groupName = intent.getExtras().getString("GroupName");
        String groupPlace = intent.getExtras().getString("GroupPlace");
        String groupGoal = intent.getExtras().getString("GroupGoal");

        String imageUrl = getIntent().getStringExtra("GroupImg");
        Glide.with(this).asBitmap().load(imageUrl).into(detailGroupImg);

        // Set items with info
        detailGroupName.setText(groupName);
        detailGroupPlace.setText(groupPlace);
        detailGroupGoal.setText(groupGoal);

        String date = timestampToString(getIntent().getExtras().getLong("addedDate"));
        detailGroupAddedDate.setText(date);


        detailChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatActivity = new Intent(getApplicationContext(), GroupChatActivity.class);
                // Go to login Activity
                startActivity(chatActivity);
                finish();
            }
        });

    }



    // Grab the image url
    private void getIncomingIntent(){
        if(getIntent().hasExtra("image_url")){

            String imageUrl = getIntent().getStringExtra("image_url");

            setImage(imageUrl);
        }
    }

    // Grab the exact image
    private void setImage(String imageUrl){

        ImageView image = findViewById(R.id.image);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(image);
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

}
