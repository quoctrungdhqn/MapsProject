package vn.trungnq.mapstutorial.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import vn.trungnq.mapstutorial.R;
import vn.trungnq.mapstutorial.data.TechcombankBranch;

public class MapAdapter implements GoogleMap.InfoWindowAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private View mView;

    public MapAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoWindow(Marker marker) {
        ViewHolder viewHolder;
        if (mView == null) {
            mView = mInflater.inflate(R.layout.item_map, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = mView.findViewById(R.id.item_map_title);
            viewHolder.tvDescription = mView.findViewById(R.id.item_map_desc);
            viewHolder.imgLogo = mView.findViewById(R.id.item_map_logo);
            mView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) mView.getTag();
        }

        TechcombankBranch.Data data = (TechcombankBranch.Data) marker.getTag();
        if (data != null) {
            viewHolder.tvTitle.setText(data.getDisplayName());
            viewHolder.tvDescription.setText(mContext.getString(R.string.tv_detail));
            if (data.getDisplayPicture() != null) {
                Picasso.with(mContext).load(data.getDisplayPicture())
                        .into(viewHolder.imgLogo, new InfoWindowRefresher(marker, data.getDisplayPicture(), viewHolder.imgLogo, mContext));
            } else {
                viewHolder.imgLogo.setImageResource(R.color.colorGrey);
            }
        }

        return mView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private static class ViewHolder {
        private TextView tvTitle;
        private TextView tvDescription;
        private ImageView imgLogo;
    }
}
