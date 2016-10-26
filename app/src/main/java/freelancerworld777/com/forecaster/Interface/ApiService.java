package freelancerworld777.com.forecaster.Interface;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by HP on 23-10-2016.
 */

public interface ApiService {
    @GET("data/2.5/weather/")
    Call<JsonObject> getMyJSONNOW(@Query("lat") String lat, @Query("lon") String lon, @Query("units") String units,
                                  @Query("APPID") String APPID);
    @GET("/data/2.5/forecast/")
    Call<JsonObject> getMyJSONHOUR(@Query("lat") String lat,@Query("lon") String lon,@Query("units") String units,
                                  @Query("APPID") String APPID);
    @GET("/data/2.5/forecast/daily/")
    Call<JsonObject> getMyJSONDAY(@Query("lat") String lat, @Query("lon") String lon,@Query("cnt") String cnt, @Query("units") String units,
                                  @Query("APPID") String APPID);
}
