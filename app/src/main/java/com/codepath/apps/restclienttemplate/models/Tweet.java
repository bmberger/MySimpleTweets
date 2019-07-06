package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;

import org.json.JSONException;
import org.json.JSONObject;

@org.parceler.Parcel
public
class Tweet {

    // list of the attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public int favoriteCount;
    public boolean favorited;

    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet();
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.favoriteCount = jsonObject.getInt("favorite_count");
        tweet.favorited = jsonObject.getBoolean("favorited");

        return tweet;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
