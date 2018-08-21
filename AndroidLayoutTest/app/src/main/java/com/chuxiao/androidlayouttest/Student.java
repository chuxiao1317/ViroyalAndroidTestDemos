package com.chuxiao.androidlayouttest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 12525 on 2018/4/11.
 */

public class Student implements Parcelable{
    public String name;

    public int age;

    public String sex;

    public Student(){

    }

    protected Student(Parcel in) {
        name = in.readString();
        age = in.readInt();
        sex = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(sex);
    }
}
