package com.github.manjunathc23.sample;

import android.os.Bundle;
import android.support.annotation.RawRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.manjunathc23.params.CircularImageOptionsParams;
import com.github.manjunathc23.views.CircularImageView;

import java.util.Random;

public class DemoActivity extends AppCompatActivity {

    private CircularImageView mImageView1;
    private CircularImageView mImageView2;
    private CircularImageView mImageView3;

    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mRandom = new Random();
        mImageView1 = (CircularImageView) findViewById(R.id.image_view_1);
        mImageView2 = (CircularImageView) findViewById(R.id.image_view_2);
        mImageView3 = (CircularImageView) findViewById(R.id.image_view_3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(getApplicationContext())
                .load(getRandomDrawable())
                .crossFade()
                .into(mImageView1);

        Glide.with(getApplicationContext())
                .load(getRandomDrawable())
                .crossFade()
                .into(mImageView2);

        Glide.with(getApplicationContext())
                .load(getRandomDrawable())
                .crossFade()
                .into(mImageView3);

        CircularImageOptionsParams params = new CircularImageOptionsParams.CircularImageBuilder()
                .setBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.civ_demo_red))
                .build();
        mImageView3.setAdditionalData(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageView1 = null;
        mImageView2 = null;
    }

    @RawRes
    private int getRandomDrawable() {
        switch (mRandom.nextInt(8)) {
            case 1:
                return R.raw.walter;
            case 2:
                return R.raw.gus;
            case 3:
                return R.raw.hank;
            case 4:
                return R.raw.jessy;
            case 5:
                return R.raw.marie;
            case 6:
                return R.raw.mike;
            case 7:
                return R.raw.saul;
            case 8:
                return R.raw.skyler;
            default:
                return R.raw.walter;
        }
    }

}
