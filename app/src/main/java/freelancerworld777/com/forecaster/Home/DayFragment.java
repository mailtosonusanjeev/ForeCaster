package freelancerworld777.com.forecaster.Home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;
import freelancerworld777.com.forecaster.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayFragment extends Fragment {
    View view;
    String units = "metric";
    String APPID = "94eed8f62746ea466675d0b54a8f095f";
    String cnt ="10";
    RecyclerView day_recycler;
    DayAdapter adapter;
    private JSONArray list_data;


    public DayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_day, container, false);
        day_recycler = (RecyclerView)view.findViewById(R.id.day_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        day_recycler.setLayoutManager(mLayoutManager);
        day_recycler.setItemAnimator(new DefaultItemAnimator());
        day_recycler.setAdapter(adapter);
        return view;
    }



    public void getDayForeCast(JSONArray day_list) {
        System.out.println("Day response : "+ day_list);
        list_data = day_list;

        adapter = new DayAdapter(getActivity(),list_data);
        adapter.notifyDataSetChanged();

    }


}
