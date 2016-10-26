package freelancerworld777.com.forecaster.Home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


import freelancerworld777.com.forecaster.R;

/**
 * Created by HP on 24-10-2016.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.MyViewHolder> {
    private Context mContext;
    private JSONArray day_array;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView day_date,day_temp,day_max_temp,day_min_temp,day_desc;


        public MyViewHolder(View view){
            super(view);
            day_date = (TextView)view.findViewById(R.id.day_date);
            day_desc = (TextView)view.findViewById(R.id.day_desc);
            day_temp = (TextView)view.findViewById(R.id.day_temp);
            day_max_temp = (TextView)view.findViewById(R.id.day_max_temp);
            day_min_temp = (TextView)view.findViewById(R.id.day_min_temp);

        }
    }
    public DayAdapter(Context mContext, JSONArray day_list){
        this.mContext = mContext;
        this.day_array = day_list;
        System.out.println("Day Adapter Constructor : "+day_array);

    }
    @Override
    public DayAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_card, parent, false);
        System.out.println("Day Adapter : "+day_array);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(DayAdapter.MyViewHolder holder, int position) {

        try {
            System.out.println("Day Adapter Bind: "+day_array);
            JSONObject c = day_array.getJSONObject(position);
            JSONObject temp = c.getJSONObject("temp");
            System.out.println("Day Temp : "+temp);
            String day = temp.getString("day");
            String min = temp.getString("min");
            String max = temp.getString("max");
            System.out.println(day);
            holder.day_temp.setText(day+"°C");
            holder.day_min_temp.setText("Min : "+min+"°C");
            holder.day_max_temp.setText("Max : "+max+"°C");

            JSONArray weather = c.getJSONArray("weather");
            JSONObject d =  weather.getJSONObject(0);
            String desc = d.getString("description");
            holder.day_desc.setText(desc);

            String dt = c.getString("dt");
            Date date = new Date((long) (Float.parseFloat(dt)*1000L));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String formattedDate = sdf.format(date);
            System.out.println(formattedDate);
            final String[] split = formattedDate.split(" ");

            holder.day_date.setText(split[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        return day_array.length();

    }
}
