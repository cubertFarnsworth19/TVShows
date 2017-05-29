package com.example.android.tvshows.ui.myshows.shows;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holderShows.seasons.setText(mShowsPresenter.getNumberOfSeasons(position));
        holderShows.episodes.setText(mShowsPresenter.getNumberOfEpisodes(position));
        holderShows.inProduction.setText(mShowsPresenter.getInProduction(position));
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
        @BindView(R.id.overflow)ImageView overflow;
        boolean popupWindowShowing = false;

        public ViewHolderShows(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            layout.setOnClickListener(this);
            overflow.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view.getId() == overflow.getId()){
                if(!popupWindowShowing) {

                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View menu = inflater.inflate(R.layout.myshows_shows_overflow_menu, null);

                    popupWindowShowing = true;
                    final PopupWindow popupWindow = new PopupWindow(
                            menu,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setOutsideTouchable(true);

                    TextView remove = (TextView) menu.findViewById(R.id.remove);
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mShowsPresenter.removeShow(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            mSize--;
                            notifyItemRangeChanged(getAdapterPosition(), mSize);
                            popupWindow.dismiss();
                        }
                    });

                    popupWindow.showAsDropDown(view);
                }
                else {
                    popupWindowShowing = false;
                }

            }
            else {
                mShowsPresenter.startShowInfoActivity(mContext, getAdapterPosition());
            }

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
