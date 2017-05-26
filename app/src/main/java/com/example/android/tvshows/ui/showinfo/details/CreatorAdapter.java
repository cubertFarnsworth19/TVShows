package com.example.android.tvshows.ui.showinfo.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.tvshows.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreatorAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private DetailsContract.Presenter mPresenter;
    private int mSize = 0;

    public CreatorAdapter(DetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_info_creators_list_item,parent,false);
        return new CreatorAdapter.ViewHolderCreator(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CreatorAdapter.ViewHolderCreator holderCreator = (CreatorAdapter.ViewHolderCreator) holder;
        holderCreator.creatorName.setText(mPresenter.getCreatorName(position));
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    public void displayCreators(int size){
        mSize = size;
        notifyDataSetChanged();
    }

    class ViewHolderCreator extends RecyclerView.ViewHolder{

        @BindView(R.id.creator_name)TextView creatorName;

        public ViewHolderCreator(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
