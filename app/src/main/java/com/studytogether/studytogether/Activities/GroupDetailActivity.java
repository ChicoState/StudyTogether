package com.studytogether.studytogether.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.studytogether.studytogether.R;

public class GroupDetailActivity extends AppCompatActivity {

    TextView detailGroupName, detailGroupPlace, detailGroupGoal;
    ImageView detailGroupImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail);

        detailGroupName = findViewById(R.id.detail_group_name);
        detailGroupPlace = findViewById(R.id.detail_group_place);
        detailGroupGoal = findViewById(R.id.detail_group_goal);
        detailGroupImg = findViewById(R.id.detail_group_img);

        Intent intent = getIntent();
        String groupName = intent.getExtras().getString("GroupName");
        String groupPlace = intent.getExtras().getString("GroupPlace");
        String groupGoal = intent.getExtras().getString("GroupGoal");

        String imageUrl = getIntent().getStringExtra("GroupImg");
        Glide.with(this).asBitmap().load(imageUrl).into(detailGroupImg);

        detailGroupName.setText(groupName);
        detailGroupPlace.setText(groupPlace);
        detailGroupGoal.setText(groupGoal);

    }


    private void getIncomingIntent(){
        if(getIntent().hasExtra("image_url")){

            String imageUrl = getIntent().getStringExtra("image_url");

            setImage(imageUrl);
        }
    }


    private void setImage(String imageUrl){

        ImageView image = findViewById(R.id.image);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(image);
    }
}
