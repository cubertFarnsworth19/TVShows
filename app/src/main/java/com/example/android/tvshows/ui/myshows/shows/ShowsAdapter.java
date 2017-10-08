package com.example.android.tvshows.ui.myshows.shows;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.ui.showinfo.ShowInfoActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ShowsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Parcelable{

    private Context mContext;
    private ShowsContract.Presenter mShowsPresenter;
    private Picasso mPicasso;
    private int mSize = 0;

    public ShowsAdapter(Context context, ShowsContract.Presenter showsPresenter, Picasso picasso){
        mContext = context;
        mShowsPresenter = showsPresenter;
        mPicasso = picasso;
    }

    public void setVariables(Context context, ShowsContract.Presenter showsPresenter, Picasso picasso){
        mContext = context;
        mShowsPresenter = showsPresenter;
        mPicasso = picasso;
    }

    public void displayShows(int size){
        mSize = size;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myshows_shows_list_item,parent,false);
        return new ShowsAdapter.ViewHolderShows(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderShows holderShows = (ViewHolderShows) holder;
        holderShows.title.setText(mShowsPresenter.getTitle(position));
        mPicasso.load(mShowsPresenter.getPosterUrl(position)).into(holderShows.poster);
        holderShows.seasons.setText(mShowsPresenter.getNumberOfSeasons(mContext,position));
        holderShows.episodes.setText(mShowsPresenter.getNumberOfEpisodes(mContext,position));
        holderShows.inProduction.setText(mShowsPresenter.getInProduction(position));
        holderShows.setFavorite(mShowsPresenter.isFavorite(position));
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    class ViewHolderShows extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.show_layout) LinearLayout layout;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.poster) ImageView poster;
        @BindView(R.id.show_seasons)TextView seasons;
        @BindView(R.id.show_episodes)TextView episodes;
        @BindView(R.id.in_production)TextView inProduction;
        @BindView(R.id.favorite)ImageView favoriteIcon;
        @BindView(R.id.delete_forever)ImageView deleteForever;

        boolean favorite;

        public ViewHolderShows(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            layout.setOnClickListener(this);
            favoriteIcon.setOnClickListener(this);
            deleteForever.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view.getId() == favoriteIcon.getId()) {
                if(!favorite) {
                    favoriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
                    favorite = true;
                }
                else {
                    favoriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    favorite = false;
                }
                mShowsPresenter.setFavorite(getAdapterPosition(),favorite);
            }
            else if(view.getId() == deleteForever.getId()) {
                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.are_you_sure)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mShowsPresenter.removeShow(getAdapterPosition());
                                int adapterPosition = getAdapterPosition();
                                notifyItemRemoved(adapterPosition);
                                mSize--;
                                notifyItemRangeChanged(adapterPosition, mSize);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
            else {
                mContext.startActivity(mShowsPresenter.getIntentForShowInfoActivity(mContext,getAdapterPosition()));
            }
        }

        public void setFavorite(boolean favorite) {
            this.favorite = favorite;
            if (favorite) favoriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
            else favoriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }

    }


    protected ShowsAdapter(Parcel in) {
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
    public static final Parcelable.Creator<ShowsAdapter> CREATOR = new Parcelable.Creator<ShowsAdapter>() {
        @Override
        public ShowsAdapter createFromParcel(Parcel in) {
            return new ShowsAdapter(in);
        }

        @Override
        public ShowsAdapter[] newArray(int size) {
            return new ShowsAdapter[size];
        }
    };
}
