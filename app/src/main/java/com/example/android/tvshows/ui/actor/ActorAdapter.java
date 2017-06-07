package com.example.android.tvshows.ui.actor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.tvshows.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ActorContract.Presenter mPresenter;
    Context mContext;
    int mSize;

    public ActorAdapter(Context context,ActorContract.Presenter presenter) {
        mPresenter = presenter;
        mContext = context;
    }

    public void displayCredits(int size){
        mSize = size;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actor_tvcredit_list_item,parent,false);
        return new ActorAdapter.ViewHolderActor(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderActor holderActor = (ViewHolderActor) holder;
        holderActor.characterName.setText(mPresenter.getCharacterName(position));
        holderActor.tvShowName.setText(mPresenter.getTVShowTitle(position));
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    class ViewHolderActor extends RecyclerView.ViewHolder{

        @BindView(R.id.tvshow_name)TextView tvShowName;
        @BindView(R.id.character_name)TextView characterName;

        public ViewHolderActor(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
