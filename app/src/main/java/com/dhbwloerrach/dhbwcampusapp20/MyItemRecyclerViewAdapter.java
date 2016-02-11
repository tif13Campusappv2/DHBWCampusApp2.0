package com.dhbwloerrach.dhbwcampusapp20;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dhbwloerrach.dhbwcampusapp20.news_fragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<NewsContainer.NewsItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<NewsContainer.NewsItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).Title);
        holder.mContentView.setText(mValues.get(position).Description);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    public void UpdateContent(List<NewsContainer.NewsItem> newlist)
    {
        while (!mValues.isEmpty())
        {
            try {
                mValues.remove(mValues.size()-1);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            notifyItemRemoved(mValues.size());
        }
        if(!mValues.isEmpty()) {
            for(int i=0;i<mValues.size();i++)
                notifyItemRemoved(i);
            mValues = new ArrayList<>();
        }

        for(int i=0;i<newlist.size();i++)
        {
            mValues.add(newlist.get(i));
            notifyItemInserted(mValues.size()-1);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public NewsContainer.NewsItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.title);
            mContentView = (TextView) view.findViewById(R.id.description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
