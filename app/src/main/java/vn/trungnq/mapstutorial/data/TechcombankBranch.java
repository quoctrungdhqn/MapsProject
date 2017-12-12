package vn.trungnq.mapstutorial.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TechcombankBranch implements Parcelable {
    @SerializedName("data")
    private List<Data> data = null;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    private TechcombankBranch(Parcel in) {
        this.data = new ArrayList<>();
        in.readList(this.data, Data.class.getClassLoader());
    }

    public static final Creator<TechcombankBranch> CREATOR = new Creator<TechcombankBranch>() {
        @Override
        public TechcombankBranch createFromParcel(Parcel in) {
            return new TechcombankBranch(in);
        }

        @Override
        public TechcombankBranch[] newArray(int size) {
            return new TechcombankBranch[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public static class Data implements Parcelable {
        @SerializedName("id")
        private String id;
        @SerializedName("display_name")
        private String displayName;
        @SerializedName("display_picture")
        private String displayPicture;
        @SerializedName("address")
        private String address;
        @SerializedName("latitude")
        private double latitude;
        @SerializedName("longitude")
        private double longitude;
        @SerializedName("description")
        private String description;
        @SerializedName("phone")
        private String phone;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayPicture() {
            return displayPicture;
        }

        public void setDisplayPicture(String displayPicture) {
            this.displayPicture = displayPicture;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        Data(Parcel in) {
            id = in.readString();
            displayName = in.readString();
            displayPicture = in.readString();
            address = in.readString();
            latitude = in.readDouble();
            longitude = in.readDouble();
            description = in.readString();
            phone = in.readString();
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(displayName);
            parcel.writeString(displayPicture);
            parcel.writeString(address);
            parcel.writeDouble(latitude);
            parcel.writeDouble(longitude);
            parcel.writeString(description);
            parcel.writeString(phone);
        }
    }
}
