package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.ReplyActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceScreen;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.ComposeActivity;
import com.codepath.apps.restclienttemplate.models.ReplyActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    private final int REQUEST_CODE = 20;
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
        holder.tvName.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvUsername.setText("@" + tweet.user.screenName);
        holder.tvTimestamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.ivProfileImage);
    }

    // relative timestamp on each tweet
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //reformat string and turn "minutes ago" into "m" and "seconds ago" to "s"
        relativeDate = relativeDate.replaceAll(" second.* ago", "s");
        relativeDate = relativeDate.replaceAll(" minute.* ago", "m");
        relativeDate = relativeDate.replaceAll(" hour.* ago", "h");
        relativeDate = relativeDate.replaceAll(" day.* ago", "d");
        relativeDate = relativeDate.replaceAll(" week.* ago", "w");
        relativeDate = relativeDate.replaceAll(" month.* ago", "mo");

        return relativeDate;
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    // allows us to see the total amount of tweets
    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class that will contain our recycler view
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvName;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvPosition;
        public TextView tvTimestamp;
        public ImageView tvReply;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImageCompose);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            tvReply = (ImageView) itemView.findViewById(R.id.tvReply);
            tvPosition = (TextView) itemView.findViewById(R.id.tvPosition);

            tvReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Tweet tweet = mTweets.get(position);

                    Intent replyIntent = new Intent(v.getContext(), ReplyActivity.class);
                    replyIntent.putExtra("tweet", Parcels.wrap(tweet));
                    v.getContext().startActivity(replyIntent);
                }
            });
        }
    }
}
