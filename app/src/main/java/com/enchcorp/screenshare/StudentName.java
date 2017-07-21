package com.enchcorp.screenshare;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kiran0407 on 20/7/17.
 */

public class StudentName implements Parcelable {
    String username;
    String log_type;
    String mode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLog_type() {
        return log_type;
    }

    public void setLog_type(String log_type) {
        this.log_type = log_type;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static Creator<StudentName> getCREATOR() {
        return CREATOR;
    }

    String email;
    String uid;

    protected StudentName(Parcel in) {
        username = in.readString();
        log_type = in.readString();
        mode = in.readString();
        email = in.readString();
        uid = in.readString();
    }

    public static final Creator<StudentName> CREATOR = new Creator<StudentName>() {
        @Override
        public StudentName createFromParcel(Parcel in) {
            return new StudentName(in);
        }

        @Override
        public StudentName[] newArray(int size) {
            return new StudentName[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(log_type);
        parcel.writeString(mode);
        parcel.writeString(email);
        parcel.writeString(uid);
    }
}
