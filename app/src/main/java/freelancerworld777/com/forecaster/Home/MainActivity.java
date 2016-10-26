package freelancerworld777.com.forecaster.Home;


import android.app.ProgressDialog;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import freelancerworld777.com.forecaster.Interface.ApiService;
import freelancerworld777.com.forecaster.Interface.RetroClient;
import freelancerworld777.com.forecaster.R;
import freelancerworld777.com.forecaster.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    static String lat, lon;
    TextView location_text;
    String cityName;
    String APPID = "94eed8f62746ea466675d0b54a8f095f";
    String units = "metric";
    JSONObject nowObject;
    JSONArray list_data,day_list;
    String cnt = "10";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;



    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates = true;

    private String UPDATE_LOCATION_KEY="";
    private NowFragment nowFragment;
    private HourFragment hourFragment;
    private DayFragment dayFragment;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initToolBar();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(viewPager);

       progressBar = (ProgressBar) findViewById(R.id.progressBar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View v = navigationView.getHeaderView(0);
        location_text = (TextView) v.findViewById(R.id.location_text);
        location_text.setText(cityName);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();

        if(getIntent()!=null)
        {
            if(getIntent().getExtras()!=null && getIntent().getExtras().containsKey(Constants.LOCATION_UPDATION_KEY))
            {
                UPDATE_LOCATION_KEY = getIntent().getExtras().getString(Constants.LOCATION_UPDATION_KEY);
            }
        }
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        nowFragment = new NowFragment();
        hourFragment = new HourFragment();
        dayFragment = new DayFragment();

        adapter.addFragment(nowFragment, "NOW");
        adapter.addFragment(hourFragment, "HOUR");
        adapter.addFragment(dayFragment, "DAY");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i("Google Api:", "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if(UPDATE_LOCATION_KEY.equalsIgnoreCase("key")) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if(UPDATE_LOCATION_KEY.equalsIgnoreCase("key")) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(UPDATE_LOCATION_KEY.equalsIgnoreCase("key")) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if(UPDATE_LOCATION_KEY.equalsIgnoreCase("key")) {
            if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
                startLocationUpdates();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if(UPDATE_LOCATION_KEY.equalsIgnoreCase("key")) {
            if (mGoogleApiClient.isConnected()) {
                stopLocationUpdates();
            }
        }
    }

    @Override
    protected void onStop() {
        if(UPDATE_LOCATION_KEY.equalsIgnoreCase("key")) {
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("", "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if(UPDATE_LOCATION_KEY.equalsIgnoreCase("key")) {
            if (mCurrentLocation == null) {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                lat = String.valueOf(mCurrentLocation.getLatitude());
                lon = String.valueOf(mCurrentLocation.getLongitude());
                updateUI();
            }
        }


        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if(UPDATE_LOCATION_KEY.equalsIgnoreCase("key")) {
            if (mRequestingLocationUpdates) {
                startLocationUpdates();
            }
        }
    }

    private void updateUI() {
        if(mCurrentLocation!=null)
        {
            getForeCastInfo();
            getHourForeCast();
            getDayForeCast();
        }


    }




    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        if(UPDATE_LOCATION_KEY.equalsIgnoreCase("key")) {
            mCurrentLocation = location;


           // Toast.makeText(this, "Location changed," + mCurrentLocation.getLatitude() + mCurrentLocation.getLongitude(),
                   // Toast.LENGTH_SHORT).show();
            lat = String.valueOf(mCurrentLocation.getLatitude());
            lon = String.valueOf(mCurrentLocation.getLongitude());
            updateUI();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i("Google api:", "Connection suspended");
        if(UPDATE_LOCATION_KEY.equalsIgnoreCase("key")) {
        mGoogleApiClient.connect();}
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }



    //Implement Adapter class
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void returnForcastObject(JSONObject nowObject) {
       progressBar.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        nowFragment.getForeCastInfo(nowObject);

    }
    private void getForeCastInfo() {
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connec.getActiveNetworkInfo();
        if (activeNetwork != null) {
           /* final ProgressDialog dialog;
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage(getString(R.string.string_getting_json_message));
            dialog.show();*/

            if (lon != null && lon != null) {
                ApiService api = RetroClient.getApiService();

                Call<JsonObject> call = api.getMyJSONNOW(lat, lon, units, APPID);


                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                       // dialog.dismiss();
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Succesfull Response Obtained", Toast.LENGTH_LONG);
                            //System.out.println(response.body());

                            try {
                                nowObject = new JSONObject(response.body().toString());
                                System.out.println("nowObject : " + nowObject);
                                returnForcastObject(nowObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(getApplicationContext(), R.string.string_some_thing_wrong, Toast.LENGTH_LONG);
                        }
                    }



                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.string_unable_to_fetch_json, Toast.LENGTH_LONG);
                        //dialog.dismiss();
                    }
                });


            } else {
                Toast.makeText(getApplicationContext(), R.string.string_internet_connection_not_available, Toast.LENGTH_LONG);
            }
        }
    }


    // Api Call for Hour Fragment
    private void getHourForeCast() {
        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connec.getActiveNetworkInfo();
        if (activeNetwork != null) {
           /* final ProgressDialog dialog;
            dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.string_getting_json_message));
            dialog.show();*/

            //Call for Http Connection
            ApiService api = RetroClient.getApiService();
            //Call<ProductList> call = api.getMyJSON();
            Call<JsonObject> call = api.getMyJSONHOUR(lat, lon, units, APPID);


            call.enqueue(new Callback<JsonObject>(){
                @Override
                public  void onResponse(Call<JsonObject> call, Response<JsonObject> response){
                    //dialog.dismiss();
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Succesfull Response Obtained", Toast.LENGTH_LONG);
                        System.out.println(response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if(jsonObject.has("list")){
                                list_data = jsonObject.getJSONArray("list");
                                System.out.println(list_data);
                                returnHourForecast(list_data);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }else {
                        Toast.makeText(getApplicationContext(), R.string.string_some_thing_wrong, Toast.LENGTH_LONG);
                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t){
                    Toast.makeText(getApplicationContext(), R.string.string_unable_to_fetch_json, Toast.LENGTH_LONG);
                   // dialog.dismiss();
                }
            });


        }else {
            Toast.makeText(getApplicationContext(), R.string.string_internet_connection_not_available, Toast.LENGTH_LONG);
        }
    }
    private void returnHourForecast(JSONArray list_data){
        hourFragment.getHourForeCast(list_data);
    }

    // Api Call for Day forecast
    private void getDayForeCast() {
        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connec.getActiveNetworkInfo();
        if (activeNetwork != null) {
            /*final ProgressDialog dialog;
            dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.string_getting_json_message));
            dialog.show();*/

            //Call for Http Connection
            ApiService api = RetroClient.getApiService();
            //Call<ProductList> call = api.getMyJSON();
            Call<JsonObject> call = api.getMyJSONDAY(lat, lon, cnt, units, APPID);


            call.enqueue(new Callback<JsonObject>(){
                @Override
                public  void onResponse(Call<JsonObject> call, Response<JsonObject> response){
                   // dialog.dismiss();
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Succesfull Response Obtained", Toast.LENGTH_LONG);
                        System.out.println(response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if(jsonObject.has("list")){
                                day_list = jsonObject.getJSONArray("list");
                                System.out.println(day_list);
                                returnDayForecast(day_list);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }else {
                        Toast.makeText(getApplicationContext(), R.string.string_some_thing_wrong, Toast.LENGTH_LONG);
                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t){
                    Toast.makeText(getApplicationContext(), R.string.string_unable_to_fetch_json, Toast.LENGTH_LONG);
                   // dialog.dismiss();
                }
            });


        }else {
            Toast.makeText(getApplicationContext(), R.string.string_internet_connection_not_available, Toast.LENGTH_LONG);
        }
    }

    private void returnDayForecast(final JSONArray day_list){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dayFragment.getDayForeCast(day_list);
            }
        });

    }
}
