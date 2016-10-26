package freelancerworld777.com.forecaster.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import freelancerworld777.com.forecaster.R;

/**
 * Created by HP on 24-10-2016.
 */

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.MyViewHolder> {
    private Context mContext;
    private JSONArray list_data;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView hour_time,hour_temp;


        public MyViewHolder(View view){
            super(view);
            hour_time = (TextView)view.findViewById(R.id.hour_time);
            hour_temp = (TextView)view.findViewById(R.id.hour_temp);

        }
    }
    public HourAdapter(Context mContext, JSONArray list_data){
        this.mContext = mContext;
        this.list_data = list_data;
    }
    @Override
    public HourAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hour_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HourAdapter.MyViewHolder holder, int position) {

            try {
                JSONObject c = list_data.getJSONObject(position);
                JSONObject main = c.getJSONObject("main");
                String temp = main.getString("temp");
                System.out.println("Hour temp : "+temp);
                holder.hour_temp.setText(temp+"°C");

                String time = c.getString("dt_txt");
                final String[] split = time.split(" ");
                holder.hour_time.setText(split[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }


    }

    @Override
    public int getItemCount() {
        return list_data.length();
    }
}
