package com.pixformance.themovie.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixformance.themovie.R;
import com.pixformance.themovie.data.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class SuggestionAdapter extends RecyclerView.Adapter {

    private List<String> mSuggestion = new ArrayList<>();

    private SuggestionAdapterCallback mSuggestionAdapterCallback;

    public void setSuggestionAdapterCallback(SuggestionAdapterCallback suggestionAdapterCallback) {
        this.mSuggestionAdapterCallback = suggestionAdapterCallback;
    }

    public interface SuggestionAdapterCallback {
        void onSelectSuggestion(String suggestion);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final String suggestion = mSuggestion.get(position);

        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.title.setText(suggestion);

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSuggestionAdapterCallback.onSelectSuggestion(suggestion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSuggestion.size();
    }

    public void setData(List<String> suggestion) {
        this.mSuggestion = suggestion;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view)
        View view;
        @BindView(R.id.tv_title)
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
