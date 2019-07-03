package com.codepath.apps.restclienttemplate.models;

import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TimelineActivity;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private final int RESULT_OK = 20;
    TwitterClient client;
    Tweet tweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);

        // find views and sets user information in compose tweet's views
        TextView tvFullName = (TextView) findViewById(R.id.ivNameCompose);
        TextView tvScreenName = (TextView) findViewById(R.id.ivUsernameCompose);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImageCompose);

        tvFullName.setText(getIntent().getStringExtra("ivName"));
        tvScreenName.setText(getIntent().getStringExtra("ivUsername"));
        Glide.with(this)
                .load(getIntent().getStringExtra("ivProfileURL"))
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfileImage);
    }

    public void submitTweet(View view) {
        //purpose of all of this is to have immediate loading of new Tweet locally without refresh
        EditText eText = (EditText)findViewById(R.id.inputText);
        String tweetContent = eText.getText().toString();

        // prepare data intent and pass relevant data back as a result
        final Intent data = new Intent(ComposeActivity.this, TimelineActivity.class);
        data.putExtra("status", tweetContent);
        setResult(RESULT_OK, data); // set result code and bundle data for response

        client.sendTweet(tweetContent, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    tweet = Tweet.fromJSON(response);
                    // sends back to timeline
                    data.putExtra("tweet", Parcels.wrap(tweet));
                    startActivity(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("Tweet failed to post", "API couldn't make a correct call with tweet", throwable);
            }
        });
        finish(); // closes the activity, pass data to parent
    }
}
