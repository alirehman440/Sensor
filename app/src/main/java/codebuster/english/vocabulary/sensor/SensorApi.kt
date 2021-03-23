package codebuster.english.vocabulary.sensor

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface SensorApi {


    @POST("get_live_data_for_dev_test")
    fun ApiName(@Body jsonBody: JsonObject): Call<DataClass>

}
