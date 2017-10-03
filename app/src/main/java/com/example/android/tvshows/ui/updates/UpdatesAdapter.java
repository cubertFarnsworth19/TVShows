package com.example.android.tvshows.ui.updates;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.tvshows.R;
import com.example.android.tvshows.idlingResource.SimpleIdlingResource;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
/*
    Parcelable has not been implemented properly but works for device rotation
 */
public class UpdatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Parcelable{

    Context mContext;
    UpdatesContract.Presenter mUpdatesPresenter;
    private int mSize;
    ArrayList<Boolean> mAllChecked;
    ArrayList<ArrayList<Boolean>> mChecked;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    public UpdatesAdapter(Context context, UpdatesContract.Presenter updatesPresenter) {
        mContext = context;
        mUpdatesPresenter = updatesPresenter;
    }

    public void displayUpdates(int size){
        mSize = size;
        mAllChecked = new ArrayList<>(mSize);
        for(int i=0;i<mSize;i++){
            mAllChecked.add(false);
        }

        mChecked = new ArrayList<>(mSize);

        for(int i=0;i<mSize;i++){
            int individualUpdates = mUpdatesPresenter.getNumberOfSeasons(i);
            ArrayList<Boolean> seasonsChecked = new ArrayList<>(individualUpdates);
            for(int j=0;j<individualUpdates+1;j++){
                seasonsChecked.add(false);
            }
            mChecked.add(seasonsChecked);
        }
        notifyDataSetChanged();
    }

    public void displayUpdates(){
        notifyDataSetChanged();
    }

    public void uncheckAll(){
        for(int i=0;i< mAllChecked.size();i++){
            mAllChecked.set(i,false);
        }

        for(int i=0;i< mChecked.size();i++){
            for(int j=0;j< mChecked.get(i).size();j++){
                mChecked.get(i).set(j,false);
            }
        }

        notifyDataSetChanged();
    }

    public ArrayList<Pair<Boolean,ArrayList<Boolean>>> getChecked(){
        ArrayList<Pair<Boolean,ArrayList<Boolean>>> checked = new ArrayList<>();

        for(int i=0;i<mChecked.size();i++){
            ArrayList<Boolean> seasonsChecked = new ArrayList<>();
            for (int j=1;j<mChecked.get(i).size();j++)
                seasonsChecked.add(mChecked.get(i).get(j));

            Pair<Boolean,ArrayList<Boolean>> pair = new Pair<>(mChecked.get(i).get(0),seasonsChecked);
            checked.add(pair);
        }

        return checked;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_list_item,parent,false);
        return new ViewHolderUpdates(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolderUpdates holderUpdates = (ViewHolderUpdates) holder;
        holderUpdates.setIsRecyclable(false);
        holderUpdates.showName.setText(mUpdatesPresenter.getShowName(position));
        holderUpdates.lastUpdate.setText(mUpdatesPresenter.getLastUpdate(position));
        holderUpdates.selectAll.setChecked(mAllChecked.get(position));
        holderUpdates.expandableLayout.setInRecyclerView(true);
        holderUpdates.setRecyclerView();
        holderUpdates.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreClose() {
                createRotateAnimator(holderUpdates.showSeasons,180f,0f).start();
            }

            @Override
            public void onPreOpen() {
                createRotateAnimator(holderUpdates.showSeasons,0f,180f).start();
            }

            @Override
            public void onAnimationEnd() {
                super.onAnimationEnd();
                if (mIdlingResource != null) mIdlingResource.setIdleState(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    class ViewHolderUpdates extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.button_individual) RelativeLayout showSeasons;
        @BindView(R.id.check_box_select_all) CheckBox selectAll;
        @BindView(R.id.show_name) TextView showName;
        @BindView(R.id.last_update) TextView lastUpdate;
        @BindView(R.id.recyclerview_individual)RecyclerView recyclerView;
        @BindView(R.id.expandable_linear_layout)ExpandableLinearLayout expandableLayout;
        IndividualAdapter individualAdapter;
        boolean expanded = false;

        public ViewHolderUpdates(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            showSeasons.setOnClickListener(this);
            selectAll.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == showSeasons.getId()){
                if (mIdlingResource != null) mIdlingResource.setIdleState(true);
                expandableLayout.toggle();
                expanded = !expanded;
                if (expanded) individualAdapter.updateChecked();
            }
            else if(view.getId() == selectAll.getId()){

               for(int i=0;i<mChecked.get(getAdapterPosition()).size();i++)
                   mChecked.get(getAdapterPosition()).set(i,selectAll.isChecked());


                if (expanded) individualAdapter.updateChecked();

                mAllChecked.set(getAdapterPosition(),selectAll.isChecked());
            }
        }

        void setRecyclerView(){
            individualAdapter = new IndividualAdapter(getAdapterPosition(),
                    mUpdatesPresenter.getNumberOfSeasons(getAdapterPosition())+1,
                    mUpdatesPresenter.getShowId(getAdapterPosition()),
                    mChecked.get(getAdapterPosition()),
                    this);

            recyclerView.setAdapter(individualAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setNestedScrollingEnabled(false);
        }

        void childUnchecked(){
            selectAll.setChecked(false);
        }
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    class IndividualAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        int mParentPosition;
        int mSize;
        int mShowId;
        ArrayList<Boolean> mIndividualChecked;
        ViewHolderUpdates mViewHolderUpdates;

        public IndividualAdapter(int parentPosition,int size,int showId,ArrayList<Boolean> checked,ViewHolderUpdates viewHolderUpdates) {
            mParentPosition = parentPosition;
            mSize = size;
            mShowId = showId;
            mIndividualChecked = checked;
            mViewHolderUpdates = viewHolderUpdates;

        }

        public void updateChecked(){
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_individual_list_item,parent,false);
            return new ViewHolderIndividual(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolderIndividual holderIndividual = (ViewHolderIndividual) holder;
            if (position==0){
                holderIndividual.seasonName.setText(mContext.getString(R.string.details_update));
                holderIndividual.lastUpdate.setText(mUpdatesPresenter.getShowLastUpdate(mParentPosition));
            }
            else {
                holderIndividual.seasonName.setText(mUpdatesPresenter.getSeasonName(mShowId, position - 1));
                holderIndividual.lastUpdate.setText(mUpdatesPresenter.getSeasonLastUpdate(mShowId, position - 1));
            }
            holderIndividual.update.setChecked(mIndividualChecked.get(position));
        }

        @Override
        public int getItemCount() {
            return mSize;
        }

        class ViewHolderIndividual extends RecyclerView.ViewHolder implements View.OnClickListener{

            @BindView(R.id.season_name)TextView seasonName;
            @BindView(R.id.last_update)TextView lastUpdate;
            @BindView(R.id.check_box_update) CheckBox update;

            public ViewHolderIndividual(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
                update.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (view.getId() == update.getId()){
                    mChecked.get(mParentPosition).set(getAdapterPosition(),update.isChecked());
                    if (!update.isChecked()) {
                        mAllChecked.set(mParentPosition, false);
                        mViewHolderUpdates.childUnchecked();
                    }
                }
            }

        }
    }



    protected UpdatesAdapter(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UpdatesAdapter> CREATOR = new Parcelable.Creator<UpdatesAdapter>() {
        @Override
        public UpdatesAdapter createFromParcel(Parcel in) {
            return new UpdatesAdapter(in);
        }

        @Override
        public UpdatesAdapter[] newArray(int size) {
            return new UpdatesAdapter[size];
        }
    };

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @VisibleForTesting
    public void setNotIdle(){
        if (mIdlingResource != null) mIdlingResource.setIdleState(false);
    }
}
