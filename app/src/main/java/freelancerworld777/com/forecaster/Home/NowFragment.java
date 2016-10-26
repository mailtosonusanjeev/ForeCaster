package freelancerworld777.com.forecaster.Home;


import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import freelancerworld777.com.forecaster.Interface.ApiService;
import freelancerworld777.com.forecaster.Interface.RetroClient;
import freelancerworld777.com.forecaster.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowFragment extends Fragment {
    String APPID = "94eed8f62746ea466675d0b54a8f095f";
    View view;
    String units = "metric";
    TextView now_place, now_wheather, now_temp, now_humidity, now_wind, now_pressure,lat,lon,max,min;
    CardView now_cardview;
    ImageView now_image;


    public NowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_now, container, false);
        now_cardview = (CardView) view.findViewById(R.id.now_cardview);

        now_place = (TextView) view.findViewById(R.id.now_place);
        now_wheather = (TextView) view.findViewById(R.id.now_wheather);
        now_temp = (TextView) view.findViewById(R.id.now_temp);
        now_humidity = (TextView) view.findViewById(R.id.now_humidity);
        now_pressure = (TextView) view.findViewById(R.id.now_pressure);
        now_wind = (TextView) view.findViewById(R.id.now_wind);
        now_image = (ImageView) view.findViewById(R.id.now_image);
        lat = (TextView) view.findViewById(R.id.lat);
        lon = (TextView) view.findViewById(R.id.lon);
        min = (TextView) view.findViewById(R.id.min);
        max = (TextView) view.findViewById(R.id.max);





        return view;
    }

    public void getForeCastInfo(JSONObject nowObject) {
        if (nowObject != null) {
            try {

                System.out.println("Now frag now objt : " + nowObject);
                now_place.setText(nowObject.getString("name"));
                if (nowObject.has("main")) {
                    JSONObject mainObject = nowObject.getJSONObject("main");
                    String temp = mainObject.getString("temp");
                    String min1 = mainObject.getString("temp_min");
                    String max1 = mainObject.getString("temp_max");
                    String pressure = mainObject.getString("pressure");
                    String humidity = mainObject.getString("humidity");
                    now_humidity.setText("HUMIDITY : " + humidity + "%");
                    now_pressure.setText("PRESSURE : " + pressure + "hPa");
                    now_temp.setText(temp + "°C");
                    max.setText("Maximum Temperature : " + max1 + "°C");
                    min.setText("Minimum Temperature : " + min1 + "°C");
                }
                if (nowObject.has("wind")) {
                    JSONObject wind = nowObject.getJSONObject("wind");
                    String speed = wind.getString("speed");
                    now_wind.setText("WIND SPEED : " + speed + "meter/sec");

                }
                if (nowObject.has("weather")) {
                    JSONArray weather = nowObject.getJSONArray("weather");
                    for (int i = 0; i < weather.length(); i++) {
                        JSONObject c = weather.getJSONObject(i);
                        String desc = c.getString("description");
                        now_wheather.setText(desc);

                    }
                }
                if (nowObject.has("coord")) {
                    JSONObject coord = nowObject.getJSONObject("coord");
                    String lat1 = coord.getString("lat");
                    String lon1 = coord.getString("lon");
                    lat.setText("Current Lattitude : " + lat1);
                    lon.setText("Current Longitude : " + lon1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
