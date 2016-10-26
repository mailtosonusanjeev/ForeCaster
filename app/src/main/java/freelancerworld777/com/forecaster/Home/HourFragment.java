package freelancerworld777.com.forecaster.Home;


import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class HourFragment extends Fragment {
    View view;
    String units = "metric";
    String APPID = "94eed8f62746ea466675d0b54a8f095f";
    RecyclerView hour_recycler;
    HourAdapter adapter;
    //private JSONArray list_data;


    public HourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hour, container, false);

        hour_recycler = (RecyclerView)view.findViewById(R.id.hour_recycler);

       // adapter = new HourAdapter(getActivity(), list_data);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        hour_recycler.setLayoutManager(mLayoutManager);
        hour_recycler.setItemAnimator(new DefaultItemAnimator());
       // hour_recycler.setAdapter(adapter);


        return view;
    }
    public void getHourForeCast(JSONArray list_data){

        adapter = new HourAdapter(getActivity(),list_data);
        hour_recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
