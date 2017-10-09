package com.example.android.tvshows.ui.actor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ShowsApplication;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.model.Actor;
import com.example.android.tvshows.data.model.ExternalIds;
import com.example.android.tvshows.data.model.actortvcredits.ActorTVCredits;
import com.example.android.tvshows.util.ExternalLinks;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ActorActivity extends AppCompatActivity implements ActorContract.View{

    private final String OUTSTATE_ACTOR = "actor";
    private final String OUTSTATE_ACTOR_TV_CREDITS = "actor_tv_credits";
    private final String OUTSTATE_EXTERNAL_IDS = "external_ids";

    @BindView(R.id.actor_photo)ImageView mPhoto;
    @BindView(R.id.actor_name) TextView mName;
    @BindView(R.id.actor_biography)TextView mBiography;
    @BindView(R.id.title_biography)TextView mTitleBiography;
    @BindView(R.id.title_tv_credits)TextView mTitleTvCredits;
    @BindView(R.id.loading_indicator)ProgressBar mProgressBar;
    @BindView(R.id.recyclerview_actor)RecyclerView mRecyclerView;
    @BindView(R.id.actor_layout)View mActorLayout;

    @Inject ActorContract.Presenter mActorPresenter;
    @Inject Picasso mPicasso;
    @Inject ActorAdapter mActorAdapter;

    private int mTmdbActorId;
    private boolean mLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actor_activity);

        getWindow().setBackgroundDrawable(null);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mTmdbActorId = intent.getIntExtra(ShowsDbContract.CastEntry.COLUMN_PERSON_ID, -1);

        ShowsApplication showsApplication = (ShowsApplication) getApplication();

        if(savedInstanceState!=null) {
            mLoaded = true;

            ExternalIds externalIds = savedInstanceState.getParcelable(OUTSTATE_EXTERNAL_IDS);
            ActorTVCredits actorTVCredits = savedInstanceState.getParcelable(OUTSTATE_ACTOR_TV_CREDITS);
            Actor actor = savedInstanceState.getParcelable(OUTSTATE_ACTOR);

            ActorComponent component = DaggerActorComponent.builder()
                    .applicationComponent(showsApplication.get(this).getComponent())
                    .actorModule(showsApplication.getActorModule(this, this, mTmdbActorId,
                            externalIds,actorTVCredits,actor))
                    .build();
            component.inject(this);
        }
        else {
            mLoaded = false;

            ActorComponent component = DaggerActorComponent.builder()
                    .applicationComponent(showsApplication.get(this).getComponent())
                    .actorModule(showsApplication.getActorModule(this, this, mTmdbActorId))
                    .build();
            component.inject(this);
        }
        setupRecyclerView();
        setupToolbar();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!mLoaded)
            mActorPresenter.downloadActorData(getBaseContext());
        else
            mActorPresenter.setActorData();
    }

    private void setupRecyclerView(){
        mRecyclerView.setAdapter(mActorAdapter);
        GridLayoutManager glm = new GridLayoutManager(this,1);
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.links_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_links:
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.actor_link_menu, null);
                TextView imdb = (TextView) view.findViewById(R.id.link_imdb);
                imdb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ExternalLinks.visitIMDBActorPage(getBaseContext(),mActorPresenter.getActorIMDBId());
                    }
                });
                PopupWindow linksPopupWindow = new PopupWindow(
                        view,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                linksPopupWindow.setOutsideTouchable(true);
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int height = metrics.heightPixels;
                linksPopupWindow.showAtLocation(mActorLayout, Gravity.RIGHT,0,-(height/2 + 50));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setImage(String url) {
        mPicasso.load(getString(R.string.poster_path)+url).into(mPhoto);
    }

    @Override
    public void setName(String name) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mName.setText(name);
    }

    @Override
    public void setBiography(String biography) {
        mTitleBiography.setVisibility(View.VISIBLE);
        mBiography.setText(biography);
    }

    @Override
    public void displayCredits(int size) {
        mTitleTvCredits.setVisibility(View.VISIBLE);
        mActorAdapter.displayCredits(size);
    }

    @Override
    public void noConnection() {
        mProgressBar.setVisibility(View.INVISIBLE);
        Snackbar.make(mActorLayout, getResources().getString(R.string.no_connection), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.retry),
                        new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                mProgressBar.setVisibility(View.VISIBLE);
                                mActorPresenter.downloadActorData(getBaseContext());
                            }
                        }
                ).show();
    }

    @Override
    public void startingDownload() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(OUTSTATE_ACTOR,mActorPresenter.getActor());
        outState.putParcelable(OUTSTATE_ACTOR_TV_CREDITS,mActorPresenter.getActorTVCredits());
        outState.putParcelable(OUTSTATE_EXTERNAL_IDS,mActorPresenter.getExternalIds());
    }

    public static Intent getIntent(Context context,int personId){
        Intent intent = new Intent(context, ActorActivity.class);
        intent.putExtra(ShowsDbContract.CastEntry.COLUMN_PERSON_ID,personId);
        return intent;
    }

}
