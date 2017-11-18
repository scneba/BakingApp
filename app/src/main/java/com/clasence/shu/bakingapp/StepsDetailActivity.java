package com.clasence.shu.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.clasence.shu.bakingapp.models.StepsHelper;

import java.util.ArrayList;

/**
 * Created by Neba.
 */

public class StepsDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<StepsHelper> stepsList;
    private static  int list_position;
    private Button btnNext;
    private Button btnPrev;
    private String image;
    private boolean isLandScape=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        checkScreenOrientation();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_details_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        stepsList = getIntent().getExtras().getParcelableArrayList(MasterListFragment.STEP_LIST);
        list_position = getIntent().getExtras().getInt(MasterListFragment.LIST_POSITION);





        if(!isLandScape){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(stepsList.get(list_position).getShortDescription());
        }else{
            toolbar.setVisibility(View.GONE);

        }



        StepsHelper stepsHelper = stepsList.get(list_position);
        if (savedInstanceState == null) {
            MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
            Bundle bundle = new Bundle();
            bundle.putString(MediaPlayerFragment.VIDEO_URL, stepsHelper.getVideoUrl());
            bundle.putString(MediaPlayerFragment.STEP_DETAILS, stepsHelper.getDescription());
            bundle.putBoolean(MediaPlayerFragment.IS_lAND_SCAPE, isLandScape);
            mediaPlayerFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.player_container, mediaPlayerFragment)
                    .commit();
        }


        //setup next and previous buttons
        btnNext = (Button) findViewById(R.id.btnNext);
        btnPrev = (Button) findViewById(R.id.btnPrevious);

        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
    }

    private void checkScreenOrientation(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            isLandScape=true;
            requestFullScreen();
        }
    }


        /**
         * Request for fullscreen when in landscape
         */
    private void requestFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNext:{
                final Intent intent = new Intent(this, StepsDetailActivity.class);
                intent.putParcelableArrayListExtra(MasterListFragment.STEP_LIST, MasterListFragment.stepsList);
                intent.putExtra(MasterListFragment.LIST_POSITION, list_position==stepsList.size()-1?0:list_position+1);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.btnPrevious:{
                final Intent intent = new Intent(this, StepsDetailActivity.class);
                intent.putParcelableArrayListExtra(MasterListFragment.STEP_LIST, MasterListFragment.stepsList);
                intent.putExtra(MasterListFragment.LIST_POSITION, list_position>0?list_position-1:stepsList.size()-1);
                startActivity(intent);
                finish();
                break;
            }

        }
    }
}
