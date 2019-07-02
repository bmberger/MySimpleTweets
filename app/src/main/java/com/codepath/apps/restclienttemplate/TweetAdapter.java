package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;

    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    // invoked to create a new row (inflates layout and caches references into ViewHolder)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder holder = new ViewHolder(tweetView);

        return holder;
    }

    // bind the values of tweet based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get data according to position
        Tweet tweet = mTweets.get(position);

        // populate the views according to this data - username, body, and image via Glide
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.ivProfileImage);

    }

    // allows us to see the total amount of tweets
    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class that will contain our recycler view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImageCompose);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
        }
    }
}
