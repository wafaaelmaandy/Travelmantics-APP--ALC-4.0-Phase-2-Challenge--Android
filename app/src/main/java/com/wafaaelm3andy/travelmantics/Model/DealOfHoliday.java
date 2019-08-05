package com.wafaaelm3andy.travelmantics.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class DealOfHoliday implements Parcelable {
    String title ;
    String details ;
    String price ;
    String imgurl ;
    String id ;

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    String imgName ;
    public DealOfHoliday(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DealOfHoliday( String title, String details, String price, String imgurl,String imgName) {
        this.title = title;
        this.details = details;
        this.price = price;
        this.imgurl = imgurl;
        this.imgName=imgName;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    protected DealOfHoliday(Parcel in) {
        title = in.readString();
        details = in.readString();
        price = in.readString();
        imgurl = in.readString();
        id = in.readString();
        imgName=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(details);
        dest.writeString(price);
        dest.writeString(imgurl);
        dest.writeString(id);
        dest.writeString(imgName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DealOfHoliday> CREATOR = new Parcelable.Creator<DealOfHoliday>() {
        @Override
        public DealOfHoliday createFromParcel(Parcel in) {
            return new DealOfHoliday(in);
        }

        @Override
        public DealOfHoliday[] newArray(int size) {
            return new DealOfHoliday[size];
        }
    };
}