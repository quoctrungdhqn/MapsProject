package vn.trungnq.mapstutorial.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.util.Util;
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
import com.google.gson.Gson;

import vn.trungnq.mapstutorial.R;
import vn.trungnq.mapstutorial.adapter.MapAdapter;
import vn.trungnq.mapstutorial.data.TechcombankBranch;
import vn.trungnq.mapstutorial.databinding.ActivityMapsBinding;
import vn.trungnq.mapstutorial.util.Utils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQUEST_ACCESS_LOCATION = 2;
    private double latitude;
    private double longitude;
    private ActivityMapsBinding mBinding;
    private MapAdapter mMapAdapter;
    private View mCustomMarkerView;
    private ImageView mMarkerImageView;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        checkPermission();
        mBinding.btnLocation.setOnClickListener(view -> getCurrentLocation());

        // Initial layout custom maker
        mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_marker, null);
        mMarkerImageView = mCustomMarkerView.findViewById(R.id.img_merchant);
    }

    private void showData() {
        String json = Utils.loadJSONFromAsset(this, "techcombank_hcm.json");
        if (json != null) {
            TechcombankBranch techcombankBranch = new Gson().fromJson(json, TechcombankBranch.class);
            for (TechcombankBranch.Data data : techcombankBranch.getData()) {
                LatLng position = new LatLng(data.getLatitude(), data.getLongitude());
                createMarker(position, data);
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showData();

        // Start activity when click on map window info
        mMap.setOnInfoWindowClickListener(marker -> {
            TechcombankBranch.Data data = (TechcombankBranch.Data) marker.getTag();
            if (data != null) {
                Intent intent = new Intent(MapsActivity.this, BranchDetailActivity.class);
                intent.putExtra(Utils.BRANCH_DATA, data);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isPermissionGranted = handlePermissionResponse(requestCode, grantResults, REQUEST_ACCESS_LOCATION);
        if (isPermissionGranted) {
            getLocationGPS();
        }
    }

    private void checkPermission() {
        if (checkPermissionGranted()) {
            getLocationGPS();
        } else {
            requestPermission(REQUEST_ACCESS_LOCATION);
        }
    }

    private void getLocationGPS() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (lm != null) {
            for (String provider : lm.getAllProviders()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = lm.getLastKnownLocation(provider);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                break;
            }
        }
    }

    private void getCurrentLocation() {
        LatLng latLng = new LatLng(latitude, longitude);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
        marker.setVisible(false);
        CameraUpdate cu = CameraUpdateFactory.newLatLng(latLng);
        mMap.animateCamera(cu);
    }

    public boolean checkPermissionGranted() {
        return ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
                }
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
                }
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    public boolean handlePermissionResponse(int requestCode, int[] grantResults, int permissionRequestCode) {
        return permissionRequestCode == requestCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    private void createMarker(LatLng position, TechcombankBranch.Data data) {
        mMapAdapter = new MapAdapter(this);
        mMap.setInfoWindowAdapter(mMapAdapter);
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
                            LatLngBounds bounds = builder.build();
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 52);
                            mMap.animateCamera(cu);
                            marker.setTag(data);
                        }
                    });
        } else {
            Marker marker = mMap.addMarker(new MarkerOptions().position(position)
                    .icon(BitmapDescriptorFactory.fromBitmap(Utils.getMarkerBitmapFromResource(mCustomMarkerView, mMarkerImageView, R.color.colorGrey))));
            builder.include(marker.getPosition());
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 52);
            mMap.animateCamera(cu);
            marker.setTag(data);
        }
    }
}
