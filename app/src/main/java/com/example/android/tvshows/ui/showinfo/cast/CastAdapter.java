package com.example.android.tvshows.ui.showinfo.cast;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.android.tvshows.R;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsAdapter;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Parcelable {

    Context mContext;
    CastContract.Presenter mCastPresenter;
    Picasso mPicasso;
    int mSize = 0;

    public CastAdapter(Context context, CastContract.Presenter castPresenter, Picasso picasso){
        mContext = context;
        mCastPresenter = castPresenter;
        mPicasso = picasso;
    }

    public void setVariables(Context context, CastContract.Presenter castPresenter, Picasso picasso){
        mContext = context;
        mCastPresenter = castPresenter;
        mPicasso = picasso;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_info_cast_list_item,parent,false);
        return new CastAdapter.ViewHolderCast(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CastAdapter.ViewHolderCast holderCast = (CastAdapter.ViewHolderCast) holder;
        holderCast.characterName.setText(mCastPresenter.getCharacterName(position));
        holderCast.actorName.setText(mCastPresenter.getActorName(position));
        mPicasso.load(mCastPresenter.getPhotoUrl(mContext,position)).into(holderCast.photo);
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    public void displayCast(int size){
        mSize = size;
        notifyDataSetChanged();
    }


    class ViewHolderCast extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.character_name) TextView characterName;
        @BindView(R.id.actor_name) TextView actorName;
        @BindView(R.id.photo) ImageView photo;
        @BindView(R.id.cast_layout)LinearLayout layout;

        public ViewHolderCast(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mContext.startActivity(mCastPresenter.getIntentForActorActivity(mContext,getAdapterPosition()));
        }
    }

    protected CastAdapter(Parcel in) {
        mSize = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mSize);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CastAdapter> CREATOR = new Parcelable.Creator<CastAdapter>() {
        @Override
        public CastAdapter createFromParcel(Parcel in) {
            return new CastAdapter(in);
        }

        @Override
        public CastAdapter[] newArray(int size) {
            return new CastAdapter[size];
        }
    };


}
