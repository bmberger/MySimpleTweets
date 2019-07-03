package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

@org.parceler.Parcel
public class User {

    // list the attributes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            //return new User(in);
            return new User();
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // deserialize the JSON
    public static User fromJSON(JSONObject json) throws JSONException {
        User user = new User();

        // extract and fill the values
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url_https");

        return user;
    }
}
