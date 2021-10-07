package com.example.anilscreation.GoogleMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.anilscreation.Connectivity.Connection;
import com.example.anilscreation.MainActivity;
import com.example.anilscreation.R;
import com.example.anilscreation.SplashScreen;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class GoogleMapsFragment extends AppCompatActivity implements OnMapReadyCallback, RoutingListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final int DEFAULT_ZOOM = 15;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String TAG = "TAG";
    Location lastLocation;
    PlacesClient placesClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    Double latitude, longitude;
    GoogleMap map;
    ResultReceiver resultReceiver;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    EditText txtCurrent, txtDestination;
    Geocoder geocoder;
    String currentAddress = "";
    View mapView;
    ArrayList markerPoints = new ArrayList();
    AutoCompleteTextView txtstart, txtEnd;
    ImageView imgGo;
    private List<Polyline> polylines;
    GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private double mLatitude , mLongitude ;
    private double toLatitude, toLongitude;
    protected LatLng start;
    protected LatLng end;
    private ProgressDialog progressDialog;
    private static final int[] COLORS = new int[]{R.color.primary_dark, R.color.primary, R.color.primary_light, R.color.accent, R.color.primary_dark_material_light};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_google_maps);

        txtstart = findViewById(R.id.start);
        txtEnd = findViewById(R.id.destination);
        imgGo = findViewById(R.id.send);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Show Direction");
        polylines = new ArrayList<>();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        MapsInitializer.initialize(this);
        mGoogleApiClient.connect();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            mapView = mapFragment.getView();
        }
        mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1, mGoogleApiClient, null, null);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();

                    }
                });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(mLatitude, mLongitude));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

                map.moveCamera(center);
                map.animateCamera(zoom);
            }
        },2000);

//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0,
//                new LocationListener() {
//                    @Override
//                    public void onLocationChanged(@NonNull Location location) {
//                        mLatitude = location.getLatitude();
//                        mLongitude = location.getLongitude();
//                    }
//                });
        txtstart.setAdapter(mAdapter);
        txtEnd.setAdapter(mAdapter);

        txtstart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);

                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            // Request did not complete successfully
                            places.release();
                            return;
                        }
                        final Place place = places.get(0);

                        start = place.getLatLng();
                        mLatitude = place.getLatLng().latitude;
                        mLongitude = place.getLatLng().longitude;
                        places.release();
                    }
                });
            }
        });

        txtEnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            // Request did not complete successfully
                            places.release();
                            return;
                        }
                        // Get the Place object from the buffer.
                        final Place place = places.get(0);
                        toLatitude = place.getLatLng().latitude;
                        toLongitude = place.getLatLng().longitude;

                        end = place.getLatLng();
                    }
                });
            }
        });
        txtstart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int startNum, int before, int count) {
                if (start != null) {
                    start = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (end != null) {
                    end = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        imgGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Connection.isConnected(GoogleMapsFragment.this)) {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(GoogleMapsFragment.this).create();
                    alertDialog.setTitle("Alert!");
                    alertDialog.setMessage("Please turn on Internet Connection.");
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                startActivity(new Intent(
                                        Settings.ACTION_WIFI_SETTINGS));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    alertDialog.show();
                }else{
                    route();
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(GoogleMapsFragment.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GoogleMapsFragment.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(mLatitude, mLongitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

        map.moveCamera(center);
        map.animateCamera(zoom);

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                mAdapter.setBounds(bounds);
            }
        });
    }
    private void route() {
        if (start == null || end == null) {
            if (start == null) {
                if (txtstart.getText().length() > 0) {
                    txtstart.setError("Choose location from dropdown.");
                } else {
                    Toast.makeText(this, "Please choose a starting point.", Toast.LENGTH_SHORT).show();
                }
            }
            if (end == null) {
                if (txtEnd.getText().length() > 0) {
                    txtEnd.setError("Choose location from dropdown.");
                } else{
                    Toast.makeText(this, "Please choose a destination.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            progressDialog = ProgressDialog.show(this, "Please wait.",
                    "Fetching route information.", true);
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(start, end)
                    .build();
            routing.execute();

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(mLatitude, mLongitude));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

            map.moveCamera(center);
            map.animateCamera(zoom);

            hideKeyboard();
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public void onRoutingFailure(RouteException e) {
        progressDialog.dismiss();
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int ii) {
        progressDialog.dismiss();
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        map.moveCamera(center);


        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < arrayList.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(arrayList.get(i).getPoints());
            Polyline polyline = map.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + arrayList.get(i).getDistanceValue() + ": duration - " + arrayList.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }

        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(start);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_location_on_24));
        map.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(end);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_location_on_24));
        map.addMarker(options);

    }

    @Override
    public void onRoutingCancelled() {

    }
}