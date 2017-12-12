package vn.trungnq.mapstutorial.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import vn.trungnq.mapstutorial.R;
import vn.trungnq.mapstutorial.data.TechcombankBranch;
import vn.trungnq.mapstutorial.databinding.ActivityBranchDetailBinding;
import vn.trungnq.mapstutorial.util.Utils;

public class BranchDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityBranchDetailBinding mBinding;
    private TechcombankBranch.Data data;
    private GoogleMap mMap;
    private View mCustomMarkerView;
    private ImageView mMarkerImageView;
    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_branch_detail);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_marker, null);
        mMarkerImageView = mCustomMarkerView.findViewById(R.id.img_merchant);

        Intent intent = getIntent();
        if (intent != null) {
            data = intent.getParcelableExtra(Utils.BRANCH_DATA);
            mBinding.branchName.setText(data.getDisplayName());
            mBinding.branchAddress.setText(data.getAddress());
            mBinding.branchPhone.setText(data.getPhone());
            Glide.with(this).load(data.getDisplayPicture()).into(mBinding.branchLogo);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng position = new LatLng(data.getLatitude(), data.getLongitude());
        createMarker(position, data);
    }

    private void createMarker(LatLng position, TechcombankBranch.Data data) {
        if (data.getDisplayPicture() != null) {
            Glide.with(this)
                    .load(data.getDisplayPicture())
                    .asBitmap().centerCrop()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            Marker marker = mMap.addMarker(new MarkerOptions().position(position)
                                    .icon(BitmapDescriptorFactory.fromBitmap(Utils.getMarkerBitmapFromUrl(mCustomMarkerView, bitmap, mMarkerImageView))));
                            builder.include(marker.getPosition());
                            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(position, 16f);
                            mMap.animateCamera(cu);
                            marker.setTag(data);
                        }
                    });
        } else {
            Marker marker = mMap.addMarker(new MarkerOptions().position(position)
                    .icon(BitmapDescriptorFactory.fromBitmap(Utils.getMarkerBitmapFromResource(mCustomMarkerView, mMarkerImageView, R.color.colorGrey))));
            builder.include(marker.getPosition());
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(position, 16f);
            mMap.animateCamera(cu);
            marker.setTag(data);
        }
    }
}
