package com.codepath.apps.restclienttemplate;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

// shows all the tweets
public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;

    TextView tvFullName;
    TextView tvScreenName;
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApp.getRestClient(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.twitter_blue)));

        // find the Recycler View
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        // init the arraylist data source
        tweets = new ArrayList<>();
        // construct the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets);
        // RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        // set adapter
        rvTweets.setAdapter(tweetAdapter);

        // find views and sets user information in compose tweet's views
        tvFullName = (TextView) findViewById(R.id.ivNameCompose);
        tvScreenName = (TextView) findViewById(R.id.ivReplyToUser);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImageCompose);

        tvFullName.setText(getIntent().getStringExtra("ivName"));
        tvScreenName.setText(getIntent().getStringExtra("ivUsername"));
        Glide.with(this)
                .load(getIntent().getStringExtra("ivProfileURL"))
                .into(ivProfileImage);

        populateProfileTimeline();
    }

    private void populateProfileTimeline() {
        // get data from API with network to populate timeline
        client.getProfileTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Twitter client", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // iterate through the JSON array
                for (int i = 0; i < response.length(); i++) {
                    try {
                        // for each entry, deserialize the JSON object and convert to a Tweet model
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        // add tweet to data source
                        tweets.add(tweet);
                        // notify the adapter that we've added an item
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Twitter client", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Twitter client", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("Twitter client", errorResponse.toString());
                throwable.printStackTrace();
            }
        }, tvScreenName.toString());

    }
}
