package vn.trungnq.mapstutorial.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class InfoWindowRefresher implements Callback {
    private Marker markerToRefresh;
    private String URL;
    private ImageView userPhoto;
    private Context mContext;

    InfoWindowRefresher(Marker markerToRefresh, String URL, ImageView userPhoto, Context mContext) {
        this.markerToRefresh = markerToRefresh;
        this.URL = URL;
        this.userPhoto = userPhoto;
        this.mContext = mContext;
    }

    @Override
    public void onSuccess() {
        if (markerToRefresh != null && markerToRefresh.isInfoWindowShown()) {
            markerToRefresh.hideInfoWindow();

            Picasso.with(mContext)
                    .load(URL)
                    .into(userPhoto);

            markerToRefresh.showInfoWindow();
        }
    }

    @Override
    public void onError() {

    }
}
